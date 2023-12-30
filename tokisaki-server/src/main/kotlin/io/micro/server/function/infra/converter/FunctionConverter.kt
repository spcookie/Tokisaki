package io.micro.server.function.infra.converter

import io.micro.server.function.domain.model.entity.FunctionDO
import io.micro.server.function.infra.po.FunctionEntity
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface FunctionConverter {
    fun functionEntity2functionDO(functionEntity: FunctionEntity): FunctionDO
}