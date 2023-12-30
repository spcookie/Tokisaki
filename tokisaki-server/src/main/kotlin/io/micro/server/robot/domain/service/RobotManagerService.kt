package io.micro.server.robot.domain.service

import io.micro.core.rest.PageDTO
import io.micro.core.rest.Pageable
import io.micro.server.robot.domain.model.entity.RobotDO
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
     * @param qq ID
     * @param sse 半长连接
     * @return 服务器推送事件
     */
    fun loginQQRobot(id: Long, sse: Sse): Multi<OutboundSseEvent>

    fun closeRobot(id: Long): Uni<Unit>

    fun createRobot(robotDO: RobotDO): Uni<RobotDO>

    fun getRobotList(robotDO: RobotDO, page: Pageable): Uni<PageDTO<RobotDO>>
}