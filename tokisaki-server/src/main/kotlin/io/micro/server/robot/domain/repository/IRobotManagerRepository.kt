package io.micro.server.robot.domain.repository

import io.micro.core.robot.RobotManager
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robotManager: RobotManager): Uni<RobotManager>

    fun findRobotById(id: Long): Uni<RobotManager>

    fun existRobotByAccount(account: String): Uni<Boolean>

    fun modifyRobot(robotManager: RobotManager): Uni<RobotManager>

    fun findRobotByExample(robotManager: RobotManager, pageable: Page): Uni<List<RobotManager>>
}