package io.micro.server.robot.domain.event

import io.micro.server.robot.domain.model.entity.RobotDO
import io.smallrye.mutiny.Uni

interface IRobotEvent {

    fun publishRobotLoginSuccess(robotDO: RobotDO): Uni<Unit>

}