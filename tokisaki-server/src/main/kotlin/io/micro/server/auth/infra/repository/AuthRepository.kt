package io.micro.server.auth.infra.repository

import io.micro.core.rest.Pageable
import io.micro.core.util.converter
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infra.converter.AuthConverter
import io.micro.server.auth.infra.dao.impl.UserDAO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class AuthRepository(private val userDAO: UserDAO, private val authConverter: AuthConverter) : IAuthRepository {

    override fun registerWXLoginUser(wxLoginUserDO: WXLoginUserDO): Uni<WXLoginUserDO> {
        return Uni.createFrom()
            .item { authConverter.wxUserToUser(wxLoginUserDO) }
            .flatMap { user -> userDAO.persistAndFlush(user) }
            .invoke { user -> wxLoginUserDO.id = user.id }
            .replaceWith(wxLoginUserDO)
    }

    override fun findWXLoginUserByOpenid(openid: String): Uni<WXLoginUserDO> {
        return userDAO.selectUserByOpenid(openid)
            .onItem().ifNotNull().transform(authConverter::userToWXUser)
    }

    override fun findAuthorityById(id: Long): Uni<List<AuthorityDO>> {
        return userDAO.findById(id).map { userEntity ->
            userEntity.authorities!!.converter(authConverter::authorityDAO2authorityDO)
        }
    }

    override fun findWXLoginUserById(id: Long): Uni<WXLoginUserDO> {
        return userDAO.findById(id).map { authConverter.userToWXUser(it) }
    }

    override fun findUserById(id: Long): Uni<UserDO> {
        return userDAO.findById(id).map(authConverter::userEntity2UserDO)
    }

    override fun updateUserById(userDO: UserDO): Uni<UserDO> {
        return userDAO.findById(userDO.id!!).map {
            authConverter.userDO2UserEntity(userDO, it)
            userDO
        }
    }

    override fun findUserPage(pageable: Pageable): Uni<List<UserDO>> {
        return userDAO.findAll()
            .page(Page.of(pageable.current - 1, pageable.limit))
            .list()
            .map { it.converter(authConverter::userEntity2UserDO) }
    }

    override fun countUser(): Uni<Long> {
        return userDAO.count()
    }

}