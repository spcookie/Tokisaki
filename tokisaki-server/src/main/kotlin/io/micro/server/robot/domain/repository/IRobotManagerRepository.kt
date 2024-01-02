package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.RobotDO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robotDO: RobotDO): Uni<RobotDO>

    fun findRobotById(id: Long): Uni<RobotDO>

    fun existRobotByAccountAndUserId(account: String, id: Long): Uni<Boolean>

    fun updateRobotStateById(state: Int, id: Long): Uni<Unit>

    fun findRobotByExample(robot: RobotDO, pageable: Page): Uni<List<RobotDO>>

    fun updateRobot(robot: RobotDO): Uni<RobotDO>
}