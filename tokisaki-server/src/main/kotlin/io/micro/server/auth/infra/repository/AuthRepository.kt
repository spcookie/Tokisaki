package io.micro.server.auth.infra.repository

import io.micro.server.auth.domain.model.entity.WXLoginUser
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infra.converter.UserConverter
import io.micro.server.auth.infra.dao.UserDao
import io.micro.server.auth.infra.po.User
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class AuthRepository(private val userDao: UserDao, private val userConverter: UserConverter) : IAuthRepository {
    override fun registerUser(wxLoginUser: WXLoginUser): Uni<WXLoginUser> {
        return Uni.createFrom()
            .item { userConverter.wxUserToUser(wxLoginUser) }
            .flatMap { user -> user.persistAndFlush<User>() }
            .invoke { user -> wxLoginUser.id = user.id }
            .replaceWith(wxLoginUser)
    }

    override fun findUserByOpenid(openid: String): Uni<WXLoginUser> {
        return userDao.selectUserByOpenid(openid)
            .onItem().ifNotNull().transform(userConverter::userToWXUser)
    }
}