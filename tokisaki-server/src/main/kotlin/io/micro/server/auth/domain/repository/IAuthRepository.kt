package io.micro.server.auth.domain.repository

import io.micro.server.auth.domain.model.entity.WXLoginUser
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/11/25
 */
interface IAuthRepository {
    fun registerUser(wxLoginUser: WXLoginUser): Uni<WXLoginUser>
    fun findUserByOpenid(openid: String): Uni<WXLoginUser>
}