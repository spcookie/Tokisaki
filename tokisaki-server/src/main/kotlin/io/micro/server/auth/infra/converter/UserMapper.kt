package io.micro.server.auth.infra.converter

import io.micro.server.auth.infra.po.Authority
import jakarta.inject.Singleton

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Singleton
class UserMapper {
    fun stringToAuthority(string: String): Authority {
        return Authority().apply {
            value = string
        }
    }

    fun authorityToString(authority: Authority): String {
        return authority.value!!
    }
}