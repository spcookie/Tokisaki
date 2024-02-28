package io.micro.api.robot.converter

import io.micro.api.robot.dto.OperateFeatureFunctionDTO
import io.micro.api.robot.dto.OperateRobotDTO
import io.micro.api.robot.dto.QueryRobotContactsDTO
import io.micro.api.robot.dto.QueryRobotDTO
import io.micro.core.base.BaseEnumConvertor
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO
import io.micro.server.robot.domain.model.entity.RobotDO
import io.micro.server.robot.domain.model.valobj.RobotContacts
import org.mapstruct.BeanMapping
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI, uses = [BaseEnumConvertor::class])
interface RobotManagerConverter {

    @InheritInverseConfiguration
    fun robotManager2QueryRobotDTO(robotDO: RobotDO): QueryRobotDTO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateFeatureFunctionDTO2FeatureFunction(featureFunctionDTO: OperateFeatureFunctionDTO): FeatureFunctionDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateRobotDTO2RobotManager(dto: OperateRobotDTO): RobotDO

    fun robotContact2QueryRobotContactDTO(robotContacts: RobotContacts): QueryRobotContactsDTO

}