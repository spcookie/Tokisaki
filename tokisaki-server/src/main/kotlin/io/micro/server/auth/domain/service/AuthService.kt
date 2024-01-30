package io.micro.server.auth.domain.service

import io.micro.core.rest.PageDO
import io.micro.core.rest.Pageable
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.UserStatisticsDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.smallrye.mutiny.Uni

interface AuthService {

    fun getAuthority(): Uni<List<AuthorityDO>>

    fun getUserById(id: Long): Uni<WXLoginUserDO>

    fun updateUserById(userDO: UserDO): Uni<UserDO>

    fun getUserPage(pageable: Pageable): Uni<PageDO<UserDO>>

    fun enabledAuthority(authorityDO: AuthorityDO): Uni<Unit>

    fun getAuthorityPage(pageable: Pageable): Uni<PageDO<AuthorityDO>>

    fun getAuthorityByCode(code: String): Uni<AuthorityDO>

    fun getAuthorityCacheByCode(code: String): Uni<AuthorityDO>

    fun addAuthority(authorityDO: AuthorityDO): Uni<AuthorityDO>

    fun dispatchAuth(userDO: UserDO): Uni<Unit>

    fun getUserStatistics(): Uni<UserStatisticsDO>

}