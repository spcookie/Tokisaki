package io.micro.server.function.domain.service.impl

import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.service.AuthService
import io.micro.server.function.domain.model.entity.FunctionDO
import io.micro.server.function.domain.repository.IFunctionRepository
import io.micro.server.function.domain.service.FunctionService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FunctionServiceImpl(
    private val authService: AuthService,
    private val functionRepository: IFunctionRepository
) : FunctionService {

    @WithTransaction
    @WithSession
    override fun getUserFunctions(): Uni<List<FunctionDO>> {
        return authService.getAuthority().flatMap { authorityDOs ->
            val auth = authorityDOs.map(AuthorityDO::value)
            functionRepository.findAllFunctions()
                .map { functionDOs ->
                    functionDOs.filter { auth.contains(it.code) }
                }
        }
    }

    @WithTransaction
    @WithSession
    override fun getFunctionById(id: Long): Uni<FunctionDO> {
        return functionRepository.findById(id)
    }

}