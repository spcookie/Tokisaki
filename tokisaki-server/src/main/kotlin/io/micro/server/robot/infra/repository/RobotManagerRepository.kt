package io.micro.server.robot.infra.repository

import io.micro.core.util.converter
import io.micro.server.auth.infra.po.UserEntity
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.Switch
import io.micro.server.robot.domain.repository.IRobotManagerRepository
import io.micro.server.robot.infra.cache.SwitchCache
import io.micro.server.robot.infra.converter.RobotConverter
import io.micro.server.robot.infra.converter.RobotMapper
import io.micro.server.robot.infra.converter.SwitchConverter
import io.micro.server.robot.infra.dao.IFunctionDAO
import io.micro.server.robot.infra.dao.IRobotDAO
import io.micro.server.robot.infra.dao.IUseFunctionDAO
import io.quarkus.cache.CacheResult
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
    private val useFunctionDAO: IUseFunctionDAO,
    private val functionDAO: IFunctionDAO,
    private val switchCache: SwitchCache,
    private val switchConverter: SwitchConverter
) : IRobotManagerRepository {

    override fun saveRobotWithUser(robotDO: RobotDO): Uni<RobotDO> {
        val robot = robotConverter.robotDO2RobotEntity(robotDO)
        robot.id = null
        robot.user = UserEntity().also { it.id = robotDO.userId }
        robot.functions = null
        return robotDAO.persistAndFlush(robot)
            .invoke { entity -> robotDO.id = entity.id }
            .replaceWith(robotDO)
    }

    override fun findRobotById(id: Long): Uni<RobotDO> {
        return robotDAO.findById(id)
            .onItem().ifNotNull()
            .transform(robotConverter::robotEntity2RobotDO)
    }

    @CacheResult(cacheName = "robot")
    override fun findRobotCacheableById(id: Long): Uni<RobotDO> {
        return findRobotById(id)
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
                .map { robots -> robots.converter(robotConverter::robotEntity2RobotDO) }
        } ?: Uni.createFrom().item(listOf())
    }

    override fun countRobotByExample(robot: RobotDO, pageable: Page): Uni<Long> {
        return robotConverter.robotDO2RobotEntity(robot).let {
            robotDAO.countByExample(it, Page(pageable.index, pageable.size))
        }
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

    override fun addFeatureFunctionById(id: Long, featureFunctionDO: FeatureFunctionDO): Uni<Unit> {
        return robotDAO.findById(id).flatMap { robotEntity ->
            val useFunctionEntities = robotEntity.functions ?: mutableListOf()
            val functionEntity = robotConverter.featureFunction2UseFunctionEntity(featureFunctionDO)
            functionDAO.findById(functionEntity.function!!.id!!).map { functionEntity.function = it }
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

    override fun findFeatureFunctionsByRobotId(id: Long): Uni<List<FeatureFunctionDO>> {
        return useFunctionDAO.selectUseFunctionByRobotId(id)
            .map { it.map(robotConverter::useFunctionEntity2FeatureFunction) }
    }

    override fun findSwitchCacheByUseFunctionId(id: Long): Uni<Switch> {
        return switchCache.loadSwitchConfig(id).map(switchConverter::switchDTO2switch)
    }

    override fun saveOrUpdateSwitchByFunctionId(id: Long, switch: Switch): Uni<Switch> {
        return switchCache.setSwitchConfig(id, switchConverter.switch2SwitchDTO(switch))
            .replaceWith(switch)
    }

    override fun findRobotByUseFunctionId(id: Long): Uni<RobotDO> {
        return useFunctionDAO.findById(id)
            .onItem().ifNotNull()
            .transform { robotConverter.robotEntity2RobotDO(it.robot!!) }
    }

}