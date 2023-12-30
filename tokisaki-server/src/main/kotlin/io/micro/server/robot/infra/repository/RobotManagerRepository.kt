package io.micro.server.robot.infra.repository

import io.micro.core.exception.Throws
import io.micro.server.auth.infra.dao.impl.UserDAO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.dao.impl.RobotDAO
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RobotManagerRepository(
    private val robotConverter: RobotConverter,
    private val robotDAO: RobotDAO,
    private val userDAO: UserDAO
) : IRobotManagerRepository {

    @WithSession
    override fun saveRobotWithUser(robotDO: RobotDO): Uni<RobotDO> {
        val userId = robotDO.userId
        Throws.requireNonNull(userId, "用户ID为空")
        return userDAO.findById(userId).flatMap { user ->
            Throws.requireNonNull(user, "用户不存在")
            val robot = robotConverter.robotManager2RobotPO(robotDO)!!
            robot.user = user
            robotDAO.persistAndFlush(robot)
                .invoke { robot -> robotDO.id = robot.id }
                .replaceWith(robotDO)
        }
    }

    @WithSession
    override fun findRobotById(id: Long): Uni<RobotDO> {
        return robotDAO.findById(id).map(robotConverter::robotPO2RobotManager)
    }

    @WithSession
    override fun existRobotByAccount(account: String): Uni<Boolean> {
        return robotDAO.existByAccount(account)
    }

    @WithSession
    override fun modifyRobot(robotDO: RobotDO): Uni<RobotDO> {
        return Uni.createFrom()
            .item(robotConverter.robotManager2RobotPO(robotDO))
            .flatMap {
                if (it != null) {
                    robotDAO.flush().replaceWith(it)
                } else {
                    Uni.createFrom().item(it)
                }
            }
            .map(robotConverter::robotPO2RobotManager)
    }

    @WithSession
    override fun findRobotByExample(robotDO: RobotDO, pageable: Page): Uni<List<RobotDO>> {
        return robotConverter.robotManager2RobotPO(robotDO)?.let {
            robotDAO.findByExample(it, Page(pageable.index, pageable.size))
                .map { robots ->
                    robots.map { robot ->
                        robotConverter.robotPO2RobotManager(robot)!!
                    }
                }
        } ?: Uni.createFrom().item(listOf())
    }

}