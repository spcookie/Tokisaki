package io.micro.server.dict.infra.converter

import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.micro.server.dict.infra.po.SystemDictEntity
import org.mapstruct.BeanMapping
import org.mapstruct.InheritConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface SystemDictConverter {

    fun systemDictEntity2SystemDictDO(systemDictEntity: SystemDictEntity?): SystemDictDO?

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @InheritConfiguration
    fun systemDictDO2SystemDictEntity(systemDictEntity: SystemDictDO?): SystemDictEntity?

}