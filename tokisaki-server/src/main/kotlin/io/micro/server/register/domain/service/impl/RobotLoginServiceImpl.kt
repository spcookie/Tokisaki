package io.micro.server.register.domain.service.impl

import io.micro.core.robot.Credential
import io.micro.core.robot.Robot
import io.micro.core.robot.qq.QQRobot
import io.micro.core.robot.qq.QQRobotFactory
import io.micro.core.robot.qq.QQRobotManager
import io.micro.server.register.domain.service.RobotLoginService
import io.smallrye.mutiny.Multi
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
class RobotLoginServiceImpl(
    private val factory: QQRobotFactory,
    private val manager: QQRobotManager
) : RobotLoginService {

    /**
     * QQ号与半长连接的映射
     */
    private val oldSse = mutableMapOf<String, Sse>()

    override fun qrQQLogin(qq: String, sse: Sse): Multi<OutboundSseEvent> {
        // 先移除旧的连接
        oldSse.remove(qq)
        // 再关联新的连接
        oldSse[qq] = sse
        val robot = manager.getRobot(qq)
        return if (robot != null) {
            if (robot.state() != Robot.State.Online) {
                // 已存在机器人且未登录，则先注销机器人，可以使登录二维码失效
                manager.unregisterRobot(qq)
                // 开始扫码登录
                qrQQLoginStart(qq, sse)
            } else {
                // 若机器人已登录，则重置二维码登录
                Multi.createFrom().items(sse.newEvent("reset#"))
            }
        } else {
            // 若不存在机器人，则直接开始登录
            qrQQLoginStart(qq, sse)
        }
    }

    /**
     * 通过半长连接开始QQ扫码登录
     * @param qq QQ号
     * @param sse 半长连接
     * @return 消息事件
     */
    private fun qrQQLoginStart(qq: String, sse: Sse): Multi<OutboundSseEvent> {
        // 创建QQ机器人
        val robot = factory.create(Credential(qq)) as QQRobot
        return Multi.createFrom()
            .emitter<String> { em ->
                // 绑定登录回调函数
                qqRobotEventEmitBind(robot, em)
                // 注册机器人并开始登录
                manager.registerRobot(robot)
            }
            .map {
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
        val id = robot.identify()
        robot.obtainQRCode = { qr ->
            // 将图片转换为Base64发送
            val qrCode = Base64.getEncoder().encodeToString(qr)
            em.emit("qr#$qrCode")
        }
        robot.loginSuccess = { em.emit("success#") }
        robot.loginFail = { ex -> em.emit("fail#${ex.message}") }
        robot.loginTimeout = {
            // 登录超时时注销登录，移除QQ机器人
            manager.unregisterRobot(id)
            em.emit("timeout#")
        }
    }
}