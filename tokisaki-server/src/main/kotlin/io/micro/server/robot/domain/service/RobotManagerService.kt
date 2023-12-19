package io.micro.server.robot.domain.service

import io.micro.server.robot.domain.model.entity.RobotManager
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse

/**
 *@author Augenstern
 *@since 2023/11/23
 */
interface RobotManagerService {
    /**
     * QQ机器人扫码登录
     * @param qq QQ号
     * @param sse 半长连接
     * @return 服务器推送事件
     */
    fun qrQQLogin(qq: String, sse: Sse): Multi<OutboundSseEvent>

    fun createRobot(robotManager: RobotManager): Uni<Unit>
}