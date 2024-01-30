package io.micro.server.auth.infra.repository

import io.micro.core.rest.Pageable
import io.micro.core.util.converter
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.infra.cache.UserStatisticsCache
import io.micro.server.auth.infra.converter.AuthConverter
import io.micro.server.auth.infra.dao.impl.AuthorityDAO
import io.micro.server.auth.infra.dao.impl.UserDAO
import io.quarkus.cache.CacheResult
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class AuthRepository(
    private val userDAO: UserDAO,
    private val authConverter: AuthConverter,
    private val authorityDAO: AuthorityDAO,
    private val userStatisticsCache: UserStatisticsCache
) : IAuthRepository {

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

    override fun findAuthorityByUserId(id: Long): Uni<List<AuthorityDO>> {
        return userDAO.findById(id).map { userEntity ->
            userEntity.authorities!!.converter(authConverter::authorityEntity2authorityDO)
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

    override fun findUser(pageable: Pageable): Uni<List<UserDO>> {
        return userDAO.findAll()
            .page(Page.of(pageable.current - 1, pageable.limit))
            .list()
            .map { it.converter(authConverter::userEntity2UserDO) }
    }

    override fun countUser(): Uni<Long> {
        return userDAO.count()
    }

    override fun findAuthorityById(id: Long): Uni<AuthorityDO> {
        return authorityDAO.findById(id)
            .map(authConverter::authorityEntity2authorityDO)
    }

    override fun switchAuthorityById(id: Long): Uni<Long> {
        return authorityDAO.switchAuthorityById(id)
    }

    override fun findAuthority(pageable: Pageable): Uni<List<AuthorityDO>> {
        return authorityDAO.findAll()
            .page(Page.of(pageable.index, pageable.limit))
            .list()
            .map { it.map(authConverter::authorityEntity2authorityDO) }
    }

    override fun countAuthority(): Uni<Long> {
        return authorityDAO.count()
    }

    override fun findAuthorityByExample(authorityDO: AuthorityDO): Uni<List<AuthorityDO>> {
        return authorityDAO.findAuthorityByExample(authConverter.authorityDO2authorityEntity(authorityDO))
            .map { it.converter(authConverter::authorityEntity2authorityDO) }
    }

    @CacheResult(cacheName = "authority")
    override fun findAuthorityCacheByExample(authorityDO: AuthorityDO): Uni<List<AuthorityDO>> {
        return findAuthorityByExample(authorityDO)
    }

    override fun saveAuthority(authorityDO: AuthorityDO): Uni<AuthorityDO> {
        val entity = authConverter.authorityDO2authorityEntity(authorityDO).also { it.id = null }
        return authorityDAO.persist(entity).map(authConverter::authorityEntity2authorityDO)
    }

    override fun updateUserAuthorityRelation(id: Long, authorityDOList: List<AuthorityDO>): Uni<Unit> {
        val authorityEntityList = authorityDOList.converter(authConverter::authorityDO2authorityEntity)
        return userDAO.findById(id).map { it.authorities = authorityEntityList.toMutableSet() }.replaceWithUnit()
    }

    override fun findAllUserLastLoginTime(): Uni<Map<String, Long>> {
        return userStatisticsCache.findAllUserLastLoginTime()
    }

}