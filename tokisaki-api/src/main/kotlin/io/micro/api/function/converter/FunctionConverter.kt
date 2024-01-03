package io.micro.api.function.converter

import io.micro.api.function.dto.QueryFunctionDTO
import io.micro.server.function.domain.model.entity.FunctionDO
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface FunctionConverter {
    fun functionDO2functionDTO(functionDO: FunctionDO): QueryFunctionDTO
}