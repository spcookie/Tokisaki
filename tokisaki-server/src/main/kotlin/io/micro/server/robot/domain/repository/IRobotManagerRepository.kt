package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.RobotDO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robot: RobotDO): Uni<RobotDO>

    fun findRobotById(id: Long): Uni<RobotDO>

    fun existRobotByAccount(account: String): Uni<Boolean>

    fun modifyRobot(robot: RobotDO): Uni<RobotDO>

    fun findRobotByExample(robot: RobotDO, pageable: Page): Uni<List<RobotDO>>
}