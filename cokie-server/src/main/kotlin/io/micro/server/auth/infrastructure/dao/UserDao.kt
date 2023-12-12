package io.micro.server.auth.infrastructure.dao

import io.micro.server.auth.infrastructure.po.User
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class UserDao {
    fun selectUserByOpenid(openid: String): Uni<User> {
        return User.find("openid = ?1", openid).firstResult().map { it }
    }
}