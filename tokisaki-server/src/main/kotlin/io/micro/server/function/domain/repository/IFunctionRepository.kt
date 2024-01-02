package io.micro.server.function.domain.repository

import io.micro.server.function.domain.model.entity.FunctionDO
import io.smallrye.mutiny.Uni

interface IFunctionRepository {

    fun findAllFunctions(): Uni<List<FunctionDO>>

    fun findById(id: Long): Uni<FunctionDO>
}