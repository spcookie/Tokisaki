package io.micro.server.auth.domain.service

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.smallrye.mutiny.Uni

interface AuthService {
    fun getAuthority(): Uni<List<AuthorityDO>>
}