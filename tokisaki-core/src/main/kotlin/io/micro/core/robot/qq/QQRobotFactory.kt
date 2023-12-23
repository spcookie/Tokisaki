package io.micro.core.robot.qq

import io.micro.core.robot.Credential
import io.micro.core.robot.Robot
import io.micro.core.robot.RobotFactory
import jakarta.enterprise.context.ApplicationScoped
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.auth.BotAuthorization
import net.mamoe.mirai.utils.BotConfiguration

/**
 *@author Augenstern
 *@since 2023/11/21
 */
@ApplicationScoped
class QQRobotFactory : RobotFactory {
    override fun create(credential: Credential): Robot {
        // 凭证，这里是QQ号
        val (id, account, _) = credential
        val robot = QQRobot(id, account)
        val configuration = BotConfiguration {
            // 使用手表协议
            protocol = BotConfiguration.MiraiProtocol.ANDROID_WATCH
            // 禁止掉线自动上线，上线需要重新扫描二维码
            autoReconnectOnForceOffline = false
        }
        // 使用扫码登录
        val bot = BotFactory.newBot(account.toLong(), BotAuthorization.byQRCode(), configuration)
        robot.bot = bot
        robot.state = Robot.State.Create
        // 群消息处理
        robot.onGroupMessage = { event ->
            // TODO
            println("============================")
            println(event.message)
        }
        return robot
    }
}