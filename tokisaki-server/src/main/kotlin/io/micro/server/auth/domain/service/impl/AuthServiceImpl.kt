package io.micro.server.auth.domain.service.impl

import io.micro.core.context.AuthContext
import io.micro.core.exception.requireNonNull
import io.micro.core.rest.CommonCode
import io.micro.core.rest.CommonMsg
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.model.entity.UserDO
import io.micro.server.auth.domain.model.entity.WXLoginUserDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.domain.service.AuthService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthServiceImpl(private val authRepository: IAuthRepository) : AuthService {

    @WithSession
    override fun getAuthority(): Uni<List<AuthorityDO>> {
        val id = AuthContext.id.toLongOrNull()
        requireNonNull(id, CommonMsg.NULL_USER_ID, CommonCode.ILLEGAL_STATE)
        return authRepository.findAuthorityById(id)
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

}