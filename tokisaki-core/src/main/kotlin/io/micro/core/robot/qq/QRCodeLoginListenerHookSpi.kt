package io.micro.core.robot.qq

import io.micro.core.robot.Robot
import io.quarkus.arc.Arc
import net.mamoe.mirai.Bot
import net.mamoe.mirai.auth.QRCodeLoginListener
import net.mamoe.mirai.utils.QRCodeLoginListenerHook

class QRCodeLoginListenerHookSpi : QRCodeLoginListenerHook {

    override fun onFetchQRCode(bot: Bot, data: ByteArray) {
        val bizId = bot.getBizId()
        if (bizId != null) {
            val instance = Arc.container().instance(QQRobotManager::class.java)
            if (instance.isAvailable) {
                val manager = instance.get()
                val lifeCycle = manager.getRobot(bizId) as Robot.LifeCycle?
                lifeCycle?.loggingInListener(QQRobot.QRCodeStartEvent(data))
            }
        }
    }

    override fun onStateChanged(bot: Bot, state: QRCodeLoginListener.State) {
        println("===============")
        println("当前二维码登陆状态: ${state.name}")
        println("===============")
    }

}