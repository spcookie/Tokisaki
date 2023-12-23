package io.micro.server.robot.infra.repository

import io.micro.core.exception.Throws
import io.micro.server.auth.infra.po.User
import io.micro.server.robot.domain.model.entity.RobotManager
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.po.Robot
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RobotManagerRepository(private val robotConverter: RobotConverter) : IRobotManagerRepository {

    @WithSession
    override fun saveRobotWithUser(robotManager: RobotManager): Uni<Unit> {
        return User.findById(robotManager.userId).flatMap { user ->
            Throws.requireNull({ user }, "用户不存在")
            robotConverter.robotManager2RobotPO(robotManager)!!
                .apply { this.user = user }
                .persistAndFlush<Robot>()
                .replaceWithUnit()
        }
    }

    override fun findRobotById(id: Long): Uni<RobotManager> {
        return Robot.findById(id).map(robotConverter::robotPO2RobotManager)
    }

}