package io.micro.server.auth.domain.repository

import io.micro.server.auth.domain.entity.WXUser
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/11/25
 */
interface IAuthRepository {
    fun registerUser(wxUser: WXUser): Uni<Unit>
    fun findUserByOpenid(openid: String): Uni<WXUser>
}