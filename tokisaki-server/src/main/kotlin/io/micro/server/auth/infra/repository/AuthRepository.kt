package io.micro.server.auth.infra.repository

import io.micro.server.auth.domain.model.entity.WXUser
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infra.converter.UserConverter
import io.micro.server.auth.infra.dao.UserDao
import io.micro.server.auth.infra.po.User
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