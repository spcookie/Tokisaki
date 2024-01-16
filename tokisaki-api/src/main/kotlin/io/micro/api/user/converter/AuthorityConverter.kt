package io.micro.api.user.converter

import io.micro.api.user.dto.OperateAuthorityDTO
import io.micro.api.user.dto.QueryAuthorityDTO
import io.micro.server.auth.domain.model.entity.AuthorityDO
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
interface AuthorityConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun operateAuthorityDTO2AuthorityDO(operateAuthorityDTO: OperateAuthorityDTO): AuthorityDO

    fun authorityDO2OperateAuthorityDTO(authorityDO: AuthorityDO): OperateAuthorityDTO

    fun authorityDO2QueryAuthorityDTO(authorityDO: AuthorityDO): QueryAuthorityDTO

}