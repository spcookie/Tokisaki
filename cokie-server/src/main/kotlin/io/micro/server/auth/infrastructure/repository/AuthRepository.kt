package io.micro.server.auth.infrastructure.repository

import io.micro.server.auth.domain.entity.WXUser
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infrastructure.converter.UserConverter
import io.micro.server.auth.infrastructure.dao.UserDao
import io.micro.server.auth.infrastructure.po.User
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class AuthRepository(private val userDao: UserDao, private val userConverter: UserConverter) : IAuthRepository {
    override fun registerUser(wxUser: WXUser): Uni<Unit> {
        return Uni.createFrom().item {
            userConverter.wxUserToUser(wxUser)
        }.flatMap { user ->
            user.persistAndFlush<User>()
        }.invoke { user ->
            wxUser.id = user.id
        }.replaceWithUnit()
    }

    override fun findUserByOpenid(openid: String): Uni<WXUser> {
        return userDao.selectUserByOpenid(openid)
            .onItem().ifNotNull().transform(userConverter::userToWXUser)
    }
}