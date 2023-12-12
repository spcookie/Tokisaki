package io.micro.core.robot

/**
 *@author Augenstern
 *@since 2023/11/21
 */
interface RobotFactory {
    /**
     * 创建机器人
     * @param credential 凭证
     * @return 机器人
     */
    fun create(credential: Credential): Robot
}