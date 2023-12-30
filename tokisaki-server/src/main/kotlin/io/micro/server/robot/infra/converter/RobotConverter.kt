package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import io.micro.server.robot.infra.po.RobotEntity
import io.micro.server.robot.infra.po.UseFunctionEntity
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI, uses = [RobotMapper::class])
interface RobotConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    fun robotDO2RobotEntity(robotDO: RobotDO?): RobotEntity?

    @InheritInverseConfiguration
    fun robotEntity2RobotDO(robotPO: RobotEntity?): RobotDO?

    @Mapping(source = "function.id", target = "id")
    @Mapping(source = "function.name", target = "name")
    fun useFunctionEntity2FeatureFunction(useFunctionEntity: UseFunctionEntity?): FeatureFunction?

    @InheritInverseConfiguration
    fun featureFunction2UseFunctionEntity(featureFunction: FeatureFunction?): UseFunctionEntity?

}