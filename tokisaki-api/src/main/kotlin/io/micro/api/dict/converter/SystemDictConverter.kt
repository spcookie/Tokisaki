package io.micro.api.dict.converter

import io.micro.api.dict.dto.QuerySystemDictDTO
import io.micro.server.dict.domain.model.entity.SystemDictDO
import org.mapstruct.InheritConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI, uses = [SystemDictMapper::class])
interface SystemDictConverter {

    fun systemDictDO2QuerySystemDictDTO(systemDictDO: SystemDictDO): QuerySystemDictDTO

    @InheritConfiguration
    fun querySystemDictDTO2SystemDictDO(querySystemDictDTO: QuerySystemDictDTO): SystemDictDO

}