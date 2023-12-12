package io.micro.server.auth.infrastructure.converter

import io.micro.server.auth.infrastructure.po.Authority
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