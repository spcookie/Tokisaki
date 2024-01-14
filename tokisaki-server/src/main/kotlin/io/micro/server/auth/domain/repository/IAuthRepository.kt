package io.micro.server.auth.domain.repository

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

    fun findAuthorityById(id: Long): Uni<List<AuthorityDO>>

    fun findWXLoginUserById(id: Long): Uni<WXLoginUserDO>

    fun findUserById(id: Long): Uni<UserDO>

    fun updateUserById(userDO: UserDO): Uni<UserDO>

}