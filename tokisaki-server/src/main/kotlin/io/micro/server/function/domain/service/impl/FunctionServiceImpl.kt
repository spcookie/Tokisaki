package io.micro.server.function.domain.service.impl

import io.micro.server.function.domain.model.entity.FunctionDO
import io.micro.server.function.domain.service.FunctionService
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FunctionServiceImpl : FunctionService {
    override fun getFunctions(): Uni<List<FunctionDO>> {
        TODO("Not yet implemented")
    }
}