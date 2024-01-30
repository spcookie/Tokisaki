package io.micro.api.user.converter

import io.micro.api.user.dto.DispatchAuthDTO
import io.micro.api.user.dto.OperateUserDTO
import io.micro.api.user.dto.QueryUserDTO
import io.micro.api.user.dto.UserStatisticsDTO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.UserStatisticsDO
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = ComponentModel.CDI)
interface UserConverter {

    fun userDO2QueryUserDTO(userDO: UserDO): QueryUserDTO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateUserDTO2userDO(queryUserDTO: OperateUserDTO): UserDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun dispatchAuthDTO2UserDO(dispatchAuthDTO: DispatchAuthDTO): UserDO

    fun userStatisticsDO2userStatisticsDTO(userStatisticsDO: UserStatisticsDO): UserStatisticsDTO

}