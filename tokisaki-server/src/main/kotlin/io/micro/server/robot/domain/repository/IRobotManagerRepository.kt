package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.RobotManager
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robotManager: RobotManager): Uni<RobotManager>

    fun findRobotById(id: Long): Uni<RobotManager>

    fun existRobotByAccount(account: String): Uni<Boolean>
}