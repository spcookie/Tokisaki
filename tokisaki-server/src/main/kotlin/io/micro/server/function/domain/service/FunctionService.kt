package io.micro.server.function.domain.service

import io.micro.server.function.domain.model.entity.FunctionDO
import io.smallrye.mutiny.Uni

interface FunctionService {
    fun getUserFunctions(): Uni<List<FunctionDO>>
}