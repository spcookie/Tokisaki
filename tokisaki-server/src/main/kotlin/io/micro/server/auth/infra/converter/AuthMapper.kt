package io.micro.server.auth.infra.converter

import io.micro.server.auth.infra.po.AuthorityEntity
import jakarta.inject.Singleton

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Singleton
class AuthMapper {
    fun stringToAuthority(string: String): AuthorityEntity {
        return AuthorityEntity().apply {
            value = string
        }
    }

    fun authorityToString(authorityEntity: AuthorityEntity): String {
        return authorityEntity.value!!
    }
}