package io.micro.api.robot.converter

import io.micro.api.robot.dto.RobotManagerDTO
import io.micro.server.robot.domain.model.entity.RobotManager
import org.mapstruct.BeanMapping
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface RobotManagerConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun robotManagerDTO2RobotManager(dto: RobotManagerDTO): RobotManager

    @InheritInverseConfiguration
    fun robotManager2RobotManagerDTO(dto: RobotManager): RobotManagerDTO

}