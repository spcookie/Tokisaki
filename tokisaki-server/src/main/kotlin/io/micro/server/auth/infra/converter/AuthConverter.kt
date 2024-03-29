package io.micro.server.auth.infra.converter

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.infra.po.AuthorityEntity
import io.micro.server.auth.infra.po.UserEntity
import org.mapstruct.*
import org.mapstruct.MappingConstants.ComponentModel.CDI

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Mapper(componentModel = CDI, uses = [AuthMapper::class])
interface AuthConverter {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun wxUserToUser(wxLoginUserDO: WXLoginUserDO): UserEntity

    @InheritInverseConfiguration
    fun userToWXUser(userEntity: UserEntity): WXLoginUserDO

    fun authorityEntity2authorityDO(authority: AuthorityEntity): AuthorityDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun authorityDO2authorityEntity(authority: AuthorityDO): AuthorityEntity

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun userEntity2UserDO(userEntity: UserEntity): UserDO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "authorities", ignore = true)
    fun userDO2UserEntity(userDO: UserDO, @MappingTarget userEntity: UserEntity)

}