package io.micro.core.robot

/**
 *@author Augenstern
 *@since 2023/11/21
 */
interface RobotManager {
    /**
     * 注册机器人，并登录
     * @param robot 机器人
     */
    fun registerRobot(robot: Robot)

    /**
     * 注销机器人，关闭下线移除pool
     * @param id 机器人标识
     */
    fun unregisterRobot(id: Long)

    /**
     * 获取机器人
     * @param id 机器人标识
     * @return 机器人
     */
    fun getRobot(id: Long): Robot?
}