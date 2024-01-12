package io.micro.server.dict.infra.converter

import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.micro.server.dict.infra.po.SystemDictEntity
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface SystemDictConverter {

    fun systemDictEntity2SystemDictDO(systemDictEntity: SystemDictEntity?): SystemDictDO?

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @InheritConfiguration
    fun systemDictDO2SystemDictEntity(systemDictDO: SystemDictDO): SystemDictEntity

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun systemDictDO2SystemDictEntity(systemDictDO: SystemDictDO?, @MappingTarget systemDictEntity: SystemDictEntity)

}