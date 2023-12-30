package io.micro.server.auth.infra.dao.impl

import io.micro.server.auth.infra.dao.IUserDAO
import io.micro.server.auth.infra.po.UserEntity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class UserDAO : IUserDAO {
    override fun selectUserByOpenid(openid: String): Uni<UserEntity> {
        return find("openid = ?1", openid).firstResult().map { it }
    }
}