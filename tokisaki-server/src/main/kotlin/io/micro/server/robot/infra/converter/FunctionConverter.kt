package io.micro.server.robot.infra.converter

import io.micro.server.robot.domain.model.entity.FunctionDO
import io.micro.server.robot.infra.po.FunctionEntity
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface FunctionConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun functionEntity2functionDO(functionEntity: FunctionEntity): FunctionDO

}