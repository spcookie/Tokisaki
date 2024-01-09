package io.micro.server.auth.domain.repository

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/11/25
 */
interface IAuthRepository {

    fun registerUser(wxLoginUserDO: WXLoginUserDO): Uni<WXLoginUserDO>

    fun findUserByOpenid(openid: String): Uni<WXLoginUserDO>

    fun findAuthorityById(id: Long): Uni<List<AuthorityDO>>

    fun findUserById(id: Long): Uni<WXLoginUserDO>

}