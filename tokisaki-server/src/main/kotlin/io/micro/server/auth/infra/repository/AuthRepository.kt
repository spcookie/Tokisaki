package io.micro.server.auth.infra.repository

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infra.converter.AuthConverter
import io.micro.server.auth.infra.dao.impl.UserDAO
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class AuthRepository(private val userDAO: UserDAO, private val authConverter: AuthConverter) : IAuthRepository {

    override fun registerUser(wxLoginUserDO: WXLoginUserDO): Uni<WXLoginUserDO> {
        return Uni.createFrom()
            .item { authConverter.wxUserToUser(wxLoginUserDO) }
            .flatMap { user -> userDAO.persistAndFlush(user) }
            .invoke { user -> wxLoginUserDO.id = user.id }
            .replaceWith(wxLoginUserDO)
    }

    override fun findUserByOpenid(openid: String): Uni<WXLoginUserDO> {
        return userDAO.selectUserByOpenid(openid)
            .onItem().ifNotNull().transform(authConverter::userToWXUser)
    }

    override fun findAuthorityById(id: Long): Uni<List<AuthorityDO>> {
        return userDAO.findById(id).map { userEntity ->
            userEntity.authorities!!.let { authorities ->
                authorities.map {
                    authConverter.authorityDAO2authorityDO(it)
                }
            }
        }
    }

    override fun findUserById(id: Long): Uni<WXLoginUserDO> {
        return userDAO.findById(id).map { authConverter.userToWXUser(it) }
    }

}