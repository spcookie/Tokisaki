package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.Switch
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface IRobotManagerRepository {

    fun saveRobotWithUser(robotDO: RobotDO): Uni<RobotDO>

    fun findRobotById(id: Long): Uni<RobotDO>

    fun findRobotCacheableById(id: Long): Uni<RobotDO>

    fun existRobotByAccountAndUserId(account: String, id: Long): Uni<Boolean>

    fun updateRobotStateById(state: Int, id: Long): Uni<Unit>

    fun findRobotByExample(robot: RobotDO, pageable: Page): Uni<List<RobotDO>>

    fun countRobotByExample(robot: RobotDO, pageable: Page): Uni<Long>

    fun updateRobot(robot: RobotDO): Uni<RobotDO>

    fun addFeatureFunctionById(id: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit>

    fun existRobotByRobotIdAndUserId(robotId: Long, userId: Long): Uni<Boolean>

    fun findFeatureFunctionsByRobotId(id: Long): Uni<List<FeatureFunctionDO>>

    fun findSwitchCacheByUseFunctionId(id: Long): Uni<Switch>

    fun saveOrUpdateSwitchByFunctionId(id: Long, switch: Switch): Uni<Switch>

    fun findRobotByUseFunctionId(id: Long): Uni<RobotDO>

}