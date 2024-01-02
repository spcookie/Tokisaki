package io.micro.server.auth.domain.service.impl

import io.micro.core.context.AuthContext
import io.micro.core.exception.requireNonNull
import io.micro.core.rest.CommonCode
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.repository.IAuthRepository
import io.micro.server.auth.domain.service.AuthService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthServiceImpl(private val authRepository: IAuthRepository) : AuthService {

    override fun getAuthority(): Uni<List<AuthorityDO>> {
        val id = AuthContext.id.toLongOrNull()
        requireNonNull(id, "用户ID不存在", CommonCode.ILLEGAL_STATE)
        return authRepository.findAuthorityById(id)
    }

}