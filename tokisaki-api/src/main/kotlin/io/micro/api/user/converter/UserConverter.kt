package io.micro.api.user.converter

import io.micro.api.user.dto.OperateUserDTO
import io.micro.api.user.dto.QueryUserDTO
import io.micro.server.auth.domain.model.entity.UserDO
import org.mapstruct.Mapper

@Mapper
interface UserConverter {

    fun userDO2QueryUserDTO(userDO: UserDO): QueryUserDTO

    fun operateUserDTO2userDO(queryUserDTO: OperateUserDTO): UserDO

}