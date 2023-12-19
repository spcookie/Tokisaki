package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.RobotManager
import io.micro.server.robot.infra.po.Robot
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI, uses = [RobotMapper::class])
interface RobotConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun robotManager2RobotPO(robotManager: RobotManager): Robot

}