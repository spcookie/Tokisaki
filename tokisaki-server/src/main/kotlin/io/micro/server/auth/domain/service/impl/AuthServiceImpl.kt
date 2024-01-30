package io.micro.server.auth.domain.service.impl

import io.micro.core.context.AuthContext
import io.micro.core.exception.fail
import io.micro.core.exception.requireNonNull
import io.micro.core.rest.CommonCode
import io.micro.core.rest.CommonMsg
import io.micro.core.rest.PageDO
import io.micro.core.rest.Pageable
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.UserStatisticsDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.domain.service.AuthService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import java.time.Duration

@ApplicationScoped
class AuthServiceImpl(private val authRepository: IAuthRepository) : AuthService {

    companion object {
        private val ONLINE_SURVIVAL_MINUTES = 30
    }

    @WithSession
    override fun getAuthority(): Uni<List<AuthorityDO>> {
        val id = AuthContext.id.toLongOrNull()
        requireNonNull(id, CommonMsg.NULL_USER_ID, CommonCode.ILLEGAL_STATE)
        return authRepository.findAuthorityByUserId(id)
    }

    @WithSession
    override fun getUserById(id: Long): Uni<WXLoginUserDO> {
        return authRepository.findWXLoginUserById(id)
    }

    @WithTransaction
    @WithSession
    override fun updateUserById(userDO: UserDO): Uni<UserDO> {
        return authRepository.updateUserById(userDO)
    }

    @WithSession
    override fun getUserPage(pageable: Pageable): Uni<PageDO<UserDO>> {
        return authRepository.findUser(pageable).flatMap { list ->
            authRepository.countUser().map { PageDO.of(pageable, it, list) }
        }
    }

    @WithTransaction
    override fun enabledAuthority(authorityDO: AuthorityDO): Uni<Unit> {
        return authRepository.switchAuthorityById(authorityDO.id!!).replaceWithUnit()
    }

    @WithTransaction
    override fun getAuthorityPage(pageable: Pageable): Uni<PageDO<AuthorityDO>> {
        return authRepository.findAuthority(pageable).flatMap { authorities ->
            authRepository.countAuthority().map { PageDO.of(pageable, it, authorities) }
        }
    }

    @WithSession
    override fun getAuthorityByCode(code: String): Uni<AuthorityDO> {
        return authRepository.findAuthorityByExample(AuthorityDO().also { it.value = code })
            .map {
                if (it.isNotEmpty()) {
                    it.first()
                } else {
                    null
                }
            }
    }

    @WithSession
    override fun getAuthorityCacheByCode(code: String): Uni<AuthorityDO> {
        return authRepository.findAuthorityCacheByExample(AuthorityDO().also { it.value = code })
            .map {
                if (it.isNotEmpty()) {
                    it.first()
                } else {
                    null
                }
            }
    }

    @WithTransaction
    override fun addAuthority(authorityDO: AuthorityDO): Uni<AuthorityDO> {
        return authRepository.findAuthorityByExample(AuthorityDO().also { it.value = authorityDO.value })
            .flatMap {
                if (it.isNotEmpty()) {
                    fail(CommonMsg.SAME_AUTHORITY_VALUE, CommonCode.DUPLICATE)
                }
                authRepository.saveAuthority(authorityDO)
            }
    }

    @WithTransaction
    override fun dispatchAuth(userDO: UserDO): Uni<Unit> {
        val id = userDO.id
        requireNonNull(id)
        return authRepository.findUserById(id)
            .map { user ->
                user.addAuths = userDO.addAuths
                user.removeAuths = userDO.removeAuths
                user.dispatchAuth()
                user.authorities
            }
            .flatMap { authRepository.updateUserAuthorityRelation(id, it.toList()) }
    }

    @WithTransaction
    override fun getUserStatistics(): Uni<UserStatisticsDO> {
        return Uni.createFrom().item(UserStatisticsDO())
            .flatMap { statistics ->
                authRepository.findAllUserLastLoginTime()
                    .map { lastLoginTime ->
                        val currentTimeMillis = System.currentTimeMillis()
                        val count = lastLoginTime.filterValues {
                            Duration.ofMillis(currentTimeMillis - it).toMinutes() < ONLINE_SURVIVAL_MINUTES
                        }.count()
                        statistics.apply { onlineUserCount = count }
                    }

            }
            .flatMap { statistics ->
                authRepository.countUser().map {
                    statistics.apply { enrollUserCount = it }
                }
            }
    }

}