package io.micro.core.robot

import java.util.concurrent.ConcurrentHashMap

/**
 *@author Augenstern
 *@since 2023/11/21
 */
class RobotPool : ConcurrentHashMap<Long, Robot>(16) {
    fun put(robot: Robot) {
        this[robot.id()] = robot
    }
}