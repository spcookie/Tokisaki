package io.micro.server.robot.domain.service.impl

import io.micro.core.funsdk.Cmd
import io.micro.function.domain.strategy.FunctionContext
import io.micro.server.auth.domain.model.entity.AuthorityDO
import io.micro.server.auth.domain.service.AuthService
import io.micro.server.robot.domain.model.entity.FunctionDO
import io.micro.server.robot.domain.repository.IFunctionRepository
import io.micro.server.robot.domain.service.FunctionService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ApplicationScoped
class FunctionServiceImpl(
    private val authService: AuthService,
    private val functionRepository: IFunctionRepository,
    private val functionContext: FunctionContext,
    private val json: Json
) : FunctionService {

    @WithTransaction
    @WithSession
    override fun getUserFunctions(): Uni<List<FunctionDO>> {
        return authService.getAuthority().flatMap { authorityDOs ->
            val auth = authorityDOs.map(AuthorityDO::value)
            functionRepository.findAllFunctions()
                .invoke { functionDOs ->
                    functionDOs.filter { auth.contains(it.code) }
                        .map {
                            val cmd = Cmd.byAuth(it.code)
                            if (cmd != null) {
                                val config = functionContext.config(cmd)
                                if (config != null) {
                                    it.configHint = json.encodeToString(config.hint)
                                }
                            }
                        }
                }
        }
    }

    @WithTransaction
    @WithSession
    override fun getFunctionById(id: Long): Uni<FunctionDO> {
        return functionRepository.findById(id)
    }

}