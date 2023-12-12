package io.micro.core.robot.qq

import io.micro.core.robot.Robot
import io.micro.core.robot.RobotManager
import io.micro.core.robot.RobotPool
import jakarta.enterprise.context.ApplicationScoped
import net.mamoe.mirai.event.events.GroupMessageEvent

/**
 *@author Augenstern
 *@since 2023/11/21
 */
@ApplicationScoped
class QQRobotManager : RobotManager {

    private val pool = RobotPool()

    override fun registerRobot(robot: Robot) {
        pool.put(robot)
        robot.login()
        bindOnlineEventAdapter(robot as QQRobot)
    }

    /**
     * 绑定QQ机器人线上发布的事件的适配器方法
     * @param robot QQ机器人
     */
    private fun bindOnlineEventAdapter(robot: QQRobot) {
        // 订阅群消息
        robot.bot.eventChannel.subscribeAlways<GroupMessageEvent> {
            // 发布群消息
            robot.onlineListener(QQRobot.OnlineGroupMessageEvent(it))
        }
    }

    override fun unregisterRobot(id: String) {
        pool[id]?.let {
            pool.remove(id)
            if (it.state() != Robot.State.Closed) {
                it.close()
            }
        }
    }

    override fun getRobot(id: String): Robot? {
        return pool[id]
    }
}