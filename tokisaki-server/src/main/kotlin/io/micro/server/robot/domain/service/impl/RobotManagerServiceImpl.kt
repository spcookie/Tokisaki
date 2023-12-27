package io.micro.server.robot.domain.service.impl

import cn.hutool.core.util.StrUtil
import io.micro.core.context.AuthContext
import io.micro.core.exception.Throws
import io.micro.core.robot.Credential
import io.micro.core.robot.Robot
import io.micro.core.robot.qq.QQRobot
import io.micro.core.robot.qq.QQRobotFactory
import io.micro.core.robot.qq.QQRobotManager
import io.micro.server.robot.domain.model.entity.RobotManager
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.domain.service.RobotManagerService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.subscription.MultiEmitter
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import java.util.*

/**
 *@author Augenstern
 *@since 2023/11/23
 */
@ApplicationScoped
class RobotManagerServiceImpl(
    private val factory: QQRobotFactory,
    private val manager: QQRobotManager,
    private val robotManagerRepository: IRobotManagerRepository
) : RobotManagerService {

    /**
     * QQ号与半长连接的映射
     */
    private val oldSse = mutableMapOf<Long, Sse>()

    override fun qqRobotLogin(id: Long, sse: Sse): Multi<OutboundSseEvent> {
        return robotManagerRepository.findRobotById(id).onItem().transformToMulti { robotManager ->
            if (robotManager == null) {
                Multi.createFrom().items(sse.newEvent("fail#未找到机器人"))
            } else if (!StrUtil.equals(AuthContext.id, robotManager.userId.toString())) {
                Multi.createFrom().items(sse.newEvent("fail#非法操作"))
            } else {
                // 先移除旧的连接
                oldSse.remove(id)
                // 再关联新的连接
                oldSse[id] = sse
                val robot = manager.getRobot(id)
                if (robot != null) {
                    if (robot.state() != Robot.State.Online) {
                        // 已存在机器人且未登录，则先注销机器人，可以使登录二维码失效
                        manager.unregisterRobot(id)
                        // 开始扫码登录
                        qqRobotQRLoginStart(robotManager, sse)
                    } else {
                        // 若机器人已登录，则重置二维码登录
                        Multi.createFrom().items(sse.newEvent("reset#"))
                    }
                } else {
                    // 若不存在机器人，则直接开始登录
                    qqRobotQRLoginStart(robotManager, sse)
                }
            }
        }
    }

    override fun qqRobotLogout(id: Long): Uni<Unit> {
        TODO("Not yet implemented")
    }

    override fun createRobot(robotManager: RobotManager): Uni<RobotManager> {
        robotManager.userId = AuthContext.id.toLongOrNull()
        robotManager.paramVerify()
        return robotManagerRepository.existRobotByAccount(robotManager.account)
            .flatMap { exist ->
                if (exist) {
                    Throws.fail("已存在相同账号的机器人")
                } else {
                    robotManagerRepository.saveRobotWithUser(robotManager)
                }
            }
    }

    /**
     * 通过半长连接开始QQ扫码登录
     * @param qq QQ号
     * @param sse 半长连接
     * @return 消息事件
     */
    private fun qqRobotQRLoginStart(robotManager: RobotManager, sse: Sse): Multi<OutboundSseEvent> {
        val id = robotManager.id
        Throws.requireNonNull(id, "ID为空")
        // 创建QQ机器人
        val robot = factory.create(Credential(id, robotManager.account)) as QQRobot
        return Multi.createFrom().emitter<String> { em ->
            // 绑定登录回调函数
            qqRobotEventEmitBind(robot, em)
            // 注册机器人并开始登录
            manager.registerRobot(robot)
        }.map {
            // 发送消息事件
            sse.newEvent(it)
        }
    }

    /**
     * QQ机器人登录事件绑定
     * @param robot QQ机器人
     * @param em 事件触发器
     */
    private fun qqRobotEventEmitBind(robot: QQRobot, em: MultiEmitter<in String?>) {
        val id = robot.id()
        robot.setStateChangeListener { event ->
            when (event) {
                is QQRobot.QRCodeStartEvent -> {
                    val qrCode = Base64.getEncoder().encodeToString(event.qr)
                    em.emit("qr#$qrCode")
                }

                is QQRobot.LoginTimeoutEvent -> {
                    em.emit("timeout#")
                    em.complete()
                    manager.unregisterRobot(id)
                }

                is QQRobot.LoginSuccessEvent -> em.emit("success#")
                is QQRobot.LoginFailEvent -> em.emit("fail#${event.ex.message}")
            }
        }
    }
}