package io.micro.server.robot.domain.service

import io.micro.core.rest.PageDO
import io.micro.core.rest.Pageable
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.RobotContacts
import io.micro.server.robot.domain.model.valobj.Switch
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/23
 */
interface RobotManagerService {

    fun loginQQRobot(code: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent>

    fun closeRobot(id: Long): Uni<Unit>

    fun createRobot(robotDO: RobotDO): Uni<RobotDO>

    fun getRobotList(robotDO: RobotDO, pageable: Pageable): Uni<PageDO<RobotDO>>

    fun modifyRobotInfo(robotDO: RobotDO): Uni<RobotDO>

    fun addFeatureFunction(robotId: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit>

    fun modifyFeatureFunction(robotId: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit>

    fun addOrModifyFunctionSwitch(id: Long, switch: Switch): Uni<Switch>

    fun getFunctionSwitch(id: Long): Uni<Switch>

    fun loadContacts(id: Long): Uni<RobotContacts>

    fun getLoginCode(id: Long): Uni<String>

}