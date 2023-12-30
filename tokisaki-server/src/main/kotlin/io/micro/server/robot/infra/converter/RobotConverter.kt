package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.infra.po.RobotEntity
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI, uses = [RobotMapper::class])
interface RobotConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(source = "userId", target = "user.id")
    fun robotManager2RobotPO(robotDO: RobotDO?): RobotEntity?

    @InheritInverseConfiguration
    fun robotPO2RobotManager(robotPO: RobotEntity?): RobotDO?

}