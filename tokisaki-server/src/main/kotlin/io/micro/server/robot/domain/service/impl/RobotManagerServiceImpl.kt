package io.micro.server.robot.domain.service.impl

import io.micro.core.context.AuthContext
import io.micro.core.exception.fail
import io.micro.core.exception.requireNonNull
import io.micro.core.exception.requireTure
import io.micro.core.rest.CommonCode
import io.micro.core.rest.PageDTO
import io.micro.core.rest.Pageable
import io.micro.core.robot.Credential
import io.micro.core.robot.Robot
import io.micro.core.robot.qq.QQRobot
import io.micro.core.robot.qq.QQRobotFactory
import io.micro.core.robot.qq.QQRobotManager
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.domain.service.RobotManagerService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
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

    override fun loginQQRobot(id: Long, sse: Sse): Multi<OutboundSseEvent> {
        return robotManagerRepository.findRobotById(id).onItem().transformToMulti { robotManager ->
            if (robotManager == null) {
                Multi.createFrom().items(sse.newEvent("fail#${CommonCode.NOT_FOUND.code}"))
            } else if (AuthContext.equalId(robotManager.userId)) {
                Multi.createFrom().items(sse.newEvent("fail#${CommonCode.ILLEGAL_OPERATION.code}"))
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

    @WithTransaction
    @WithSession
    override fun closeRobot(id: Long): Uni<Unit> {
        return robotManagerRepository.findRobotById(id)
            .flatMap { robotManager ->
                requireTure(
                    value = AuthContext.equalId(robotManager.userId),
                    code = CommonCode.ILLEGAL_OPERATION
                )
                manager.unregisterRobot(id)
                robotManager.state = 5
                robotManagerRepository.modifyRobot(robotManager)
            }.replaceWithUnit()
    }

    override fun createRobot(robotDO: RobotDO): Uni<RobotDO> {
        val userId = AuthContext.id.toLongOrNull()
        requireNonNull(userId, "用户ID为空")
        robotDO.userId = userId
        robotDO.paramVerify()
        val existRobot = robotManagerRepository.existRobotByAccountAndUserId(robotDO.account!!, userId)
        return existRobot
            .flatMap { exist ->
                if (exist) {
                    fail(code = CommonCode.DUPLICATE, detail = "存在相同帐号机器人")
                } else {
                    robotManagerRepository.saveRobotWithUser(robotDO)
                }
            }
    }

    @WithTransaction
    override fun getRobotList(robotDO: RobotDO, page: Pageable): Uni<PageDTO<RobotDO>> {
        robotDO.userId = AuthContext.id.toLongOrNull()
        return robotManagerRepository.findRobotByExample(robotDO, Page.of(page.current, page.limit))
            .map { PageDTO.of(page.current, page.limit, it) }
    }

    @WithTransaction
    override fun modifyRobotInfo(robotDO: RobotDO): Uni<RobotDO> {
        return robotManagerRepository.findRobotById(robotDO.id!!).flatMap { robot ->
            requireNonNull(robot, detail = "未查询到机器人")
            requireTure(AuthContext.equalId(robot.userId), code = CommonCode.ILLEGAL_OPERATION)
            val modifyRobotDO = RobotDO().apply {
                id = robotDO.id
                name = robotDO.name
                password = robotDO.password
                remark = robotDO.remark
            }
            robotManagerRepository.updateRobot(modifyRobotDO)
        }
    }

    /**
     * 通过半长连接开始QQ扫码登录
     * @param robotDO 机器人
     * @param sse 半长连接
     * @return 消息事件
     */
    private fun qqRobotQRLoginStart(robotDO: RobotDO, sse: Sse): Multi<OutboundSseEvent> {
        val id = robotDO.id
        // 创建QQ机器人
        val robot = factory.create(Credential(id!!, robotDO.account!!)) as QQRobot
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