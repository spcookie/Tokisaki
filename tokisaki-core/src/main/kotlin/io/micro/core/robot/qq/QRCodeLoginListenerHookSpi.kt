package io.micro.core.robot.qq

import io.micro.core.robot.Robot
import io.quarkus.arc.Arc
import io.quarkus.logging.Log
import net.mamoe.mirai.Bot
import net.mamoe.mirai.auth.QRCodeLoginListener
import net.mamoe.mirai.utils.QRCodeLoginListenerHook

class QRCodeLoginListenerHookSpi : QRCodeLoginListenerHook {

    override fun onFetchQRCode(bot: Bot, data: ByteArray) {
        Log.info("机器人: ${bot.id}(${bot.nick})开始登陆")
        val bizId = bot.getBizId()
        if (bizId != null) {
            val instance = Arc.container().instance(QQRobotManager::class.java)
            if (instance.isAvailable) {
                val manager = instance.get()
                val robot = manager.getRobot(bizId)
                if (robot != null && robot is Robot.LifeCycle) {
                    robot.loggingInListener(QQRobot.QRCodeStartEvent(data))
                }
            }
        }
    }

    override fun onStateChanged(bot: Bot, state: QRCodeLoginListener.State) {
        Log.debug("机器人: ${bot.id}(${bot.nick}), 当前二维码登陆状态: ${state.name}")
        val bizId = bot.getBizId()
        if (bizId != null) {
            val instance = Arc.container().instance(QQRobotManager::class.java)
            if (instance.isAvailable) {
                val manager = instance.get()
                val robot = manager.getRobot(bizId)
                if (robot != null && robot is Robot.LifeCycle) {
                    val event = when (state) {
                        QRCodeLoginListener.State.WAITING_FOR_CONFIRM -> QQRobot.QRCodeWaitingConfirmEvent()
                        QRCodeLoginListener.State.CANCELLED -> QQRobot.QRCodeCancelledEvent()
                        QRCodeLoginListener.State.TIMEOUT -> QQRobot.QRCodeTimeoutEvent()
                        QRCodeLoginListener.State.CONFIRMED -> QQRobot.QRCodeConfirmedEvent()
                        else -> null
                    }
                    if (event != null) {
                        robot.loggingInListener(event)
                    }
                }
            }
        }
    }

}