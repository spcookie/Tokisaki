package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.RobotManager
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robotManager: RobotManager): Uni<Unit>

    fun findRobotById(id: Long): Uni<RobotManager>
}