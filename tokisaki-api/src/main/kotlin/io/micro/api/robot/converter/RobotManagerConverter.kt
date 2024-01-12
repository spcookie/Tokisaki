package io.micro.api.robot.converter

import io.micro.api.robot.dto.OperateFeatureFunctionDTO
import io.micro.api.robot.dto.OperateRobotDTO
import io.micro.api.robot.dto.QueryRobotDTO
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import org.mapstruct.BeanMapping
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface RobotManagerConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun queryRobotDTO2RobotManager(dto: QueryRobotDTO): RobotDO

    @InheritInverseConfiguration
    fun robotManager2QueryRobotDTO(dto: RobotDO): QueryRobotDTO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateFeatureFunctionDTO2FeatureFunction(featureFunctionDTO: OperateFeatureFunctionDTO): FeatureFunctionDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateRobotDTO2RobotManager(dto: OperateRobotDTO): RobotDO

}