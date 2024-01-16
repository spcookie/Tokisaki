package io.micro.api.user.converter

import io.micro.api.user.dto.OperateUserDTO
import io.micro.api.user.dto.QueryUserDTO
import io.micro.server.auth.domain.model.entity.UserDO
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface UserConverter {

    fun userDO2QueryUserDTO(userDO: UserDO): QueryUserDTO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateUserDTO2userDO(queryUserDTO: OperateUserDTO): UserDO

}