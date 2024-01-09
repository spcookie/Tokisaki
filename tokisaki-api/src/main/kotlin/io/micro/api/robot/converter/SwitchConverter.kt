package io.micro.api.robot.converter

import io.micro.api.robot.dto.QuerySwitchDTO
import io.micro.server.robot.domain.model.valobj.Switch
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface SwitchConverter {

    fun switchDTO2switch(querySwitchDTO: QuerySwitchDTO): Switch

    @InheritInverseConfiguration
    fun switch2SwitchDTO(switch: Switch): QuerySwitchDTO

}