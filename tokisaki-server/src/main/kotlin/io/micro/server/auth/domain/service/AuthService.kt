package io.micro.server.auth.domain.service

import io.micro.core.rest.PageDO
import io.micro.core.rest.Pageable
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.smallrye.mutiny.Uni

interface AuthService {

    fun getAuthority(): Uni<List<AuthorityDO>>

    fun getUserById(id: Long): Uni<WXLoginUserDO>

    fun updateUserById(userDO: UserDO): Uni<UserDO>

    fun getUserPage(pageable: Pageable): Uni<PageDO<UserDO>>
}