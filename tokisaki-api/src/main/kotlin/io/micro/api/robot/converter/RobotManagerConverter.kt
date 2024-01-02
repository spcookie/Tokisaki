package io.micro.api.robot.converter

import io.micro.api.robot.dto.FeatureFunctionDTO
import io.micro.api.robot.dto.RobotDTO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import org.mapstruct.BeanMapping
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface RobotManagerConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun robotManagerDTO2RobotManager(dto: RobotDTO): RobotDO

    @InheritInverseConfiguration
    fun robotManager2RobotManagerDTO(dto: RobotDO): RobotDTO

    fun featureFunctionDTO2FeatureFunction(featureFunctionDTO: FeatureFunctionDTO): FeatureFunction

}