package io.micro.server.robot.infra.repository

import io.micro.core.exception.requireNonNull
import io.micro.server.auth.infra.dao.IUserDAO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.converter.RobotMapper
import io.micro.server.robot.infra.dao.IRobotDAO
import io.micro.server.robot.infra.dao.IUseFunctionDAO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import java.util.function.Supplier

@ApplicationScoped
class RobotManagerRepository(
    private val robotConverter: RobotConverter,
    private val robotMapper: RobotMapper,
    private val robotDAO: IRobotDAO,
    private val userDAO: IUserDAO,
    private val useFunctionDAO: IUseFunctionDAO
) : IRobotManagerRepository {

    override fun saveRobotWithUser(robotDO: RobotDO): Uni<RobotDO> {
        val userId = robotDO.userId
        requireNonNull(userId, "用户ID为空")
        return userDAO.findById(userId).flatMap { user ->
            requireNonNull(user, "用户不存在")
            val robot = robotConverter.robotDO2RobotEntity(robotDO)
            robot.user = user
            robotDAO.persistAndFlush(robot)
                .invoke { entity -> robotDO.id = entity.id }
                .replaceWith(robotDO)
        }
    }

    override fun findRobotById(id: Long): Uni<RobotDO?> {
        return robotDAO.findById(id)
            .onItem().ifNotNull()
            .transform(robotConverter::robotEntity2RobotDO)
    }

    override fun existRobotByAccountAndUserId(account: String, id: Long): Uni<Boolean> {
        return robotDAO.existByAccount(account, id)
    }

    override fun updateRobotStateById(state: Int, id: Long): Uni<Unit> {
        return Uni.createFrom()
            .item(robotMapper.number2State(state))
            .flatMap { s ->
                robotDAO.findById(id)
                    .map { it.state = s }
                    .map { robotDAO.flush() }
            }
            .replaceWithUnit()
    }

    override fun findRobotByExample(robot: RobotDO, pageable: Page): Uni<List<RobotDO>> {
        return robotConverter.robotDO2RobotEntity(robot).let {
            robotDAO.selectByExample(it, Page(pageable.index, pageable.size))
                .map { robots ->
                    robots.map { robot ->
                        robotConverter.robotEntity2RobotDO(robot)
                    }
                }
        } ?: Uni.createFrom().item(listOf())
    }

    override fun updateRobot(robot: RobotDO): Uni<RobotDO> {
        return robotDAO.findById(robot.id!!)
            .flatMap { robotEntity ->
                robotConverter.updateRobotDO2RobotEntity(robot, robotEntity)
                val functionMap = robotEntity.functions!!.associateBy { it.id!! }
                for (function in robot.functions) {
                    functionMap[function.id]?.also {
                        robotConverter.featureFunction2UpdateUseFunctionEntity(function, it)
                    }
                }
                robotDAO.flush().replaceWith(robotEntity)
            }
            .map(robotConverter::robotEntity2RobotDO)
    }

    override fun addFeatureFunctionById(id: Long, featureFunction: FeatureFunction): Uni<Unit> {
        return robotDAO.findById(id).flatMap { robotEntity ->
            val useFunctionEntities = robotEntity.functions ?: mutableListOf()
            val functionEntity = robotConverter.featureFunction2UseFunctionEntity(featureFunction)
            useFunctionDAO.persist(functionEntity)
                .call(Supplier {
                    useFunctionEntities.add(functionEntity)
                    robotDAO.flush()
                })
                .replaceWithUnit()
        }
    }

    override fun existRobotByRobotIdAndUserId(robotId: Long, userId: Long): Uni<Boolean> {
        return robotDAO.findById(robotId).map { it.user?.id == userId }
    }

}