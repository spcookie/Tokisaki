package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.infra.event.dto.RobotDTO
import io.micro.server.robot.infra.po.RobotEntity
import io.micro.server.robot.infra.po.UseFunctionEntity
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI, uses = [RobotMapper::class])
interface RobotConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    fun robotDO2RobotEntity(robotDO: RobotDO): RobotEntity

    @InheritInverseConfiguration(name = "robotDO2RobotEntity")
    fun robotEntity2RobotDO(robotPO: RobotEntity): RobotDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "function.id", target = "refId")
    @Mapping(source = "function.name", target = "name")
    @Mapping(source = "function.code", target = "code")
    fun useFunctionEntity2FeatureFunction(useFunctionEntity: UseFunctionEntity): FeatureFunctionDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @InheritInverseConfiguration
    fun featureFunction2UseFunctionEntity(featureFunctionDO: FeatureFunctionDO): UseFunctionEntity

    @BeanMapping(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "functions", ignore = true)
    fun updateRobotDO2RobotEntity(robotDO: RobotDO?, @MappingTarget robotEntity: RobotEntity)

    @BeanMapping(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    fun featureFunction2UpdateUseFunctionEntity(
        featureFunctionDO: FeatureFunctionDO,
        @MappingTarget useFunctionEntity: UseFunctionEntity
    )

    fun robotDO2RobotDTO(robotDO: RobotDO): RobotDTO

}