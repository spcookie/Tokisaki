package io.micro.server.auth.domain.repository

import io.micro.core.rest.Pageable
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/11/25
 */
interface IAuthRepository {

    fun registerWXLoginUser(wxLoginUserDO: WXLoginUserDO): Uni<WXLoginUserDO>

    fun findWXLoginUserByOpenid(openid: String): Uni<WXLoginUserDO>

    fun findAuthorityByUserId(id: Long): Uni<List<AuthorityDO>>

    fun findWXLoginUserById(id: Long): Uni<WXLoginUserDO>

    fun findUserById(id: Long): Uni<UserDO>

    fun updateUserById(userDO: UserDO): Uni<UserDO>

    fun findUser(pageable: Pageable): Uni<List<UserDO>>

    fun countUser(): Uni<Long>

    fun findAuthorityById(id: Long): Uni<AuthorityDO>

    fun switchAuthorityById(id: Long): Uni<Long>

    fun findAuthority(pageable: Pageable): Uni<List<AuthorityDO>>

    fun countAuthority(): Uni<Long>

    fun findAuthorityByExample(authorityDO: AuthorityDO): Uni<List<AuthorityDO>>

    fun saveAuthority(authorityDO: AuthorityDO): Uni<AuthorityDO>

}