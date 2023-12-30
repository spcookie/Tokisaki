package io.micro.server.auth.infra.converter

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.infra.po.AuthorityEntity
import io.micro.server.auth.infra.po.UserEntity
import org.mapstruct.BeanMapping
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel.CDI
import org.mapstruct.ReportingPolicy

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Mapper(componentModel = CDI, uses = [AuthMapper::class])
interface AuthConverter {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun wxUserToUser(wxLoginUserDO: WXLoginUserDO): UserEntity

    fun stringToAuthority(string: MutableSet<String>): MutableSet<AuthorityEntity>

    @InheritInverseConfiguration
    fun userToWXUser(userEntity: UserEntity): WXLoginUserDO

    @InheritInverseConfiguration
    fun authorityToString(string: MutableSet<AuthorityEntity>): MutableSet<String>

    fun authorityDAO2authorityDO(authority: AuthorityEntity): AuthorityDO
}