package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.RobotManager
import io.micro.server.robot.infra.po.Robot
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI, uses = [RobotMapper::class])
interface RobotConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    fun robotManager2RobotPO(robotManager: RobotManager?): Robot?

    @InheritInverseConfiguration
    fun robotPO2RobotManager(robot: Robot?): RobotManager?

}