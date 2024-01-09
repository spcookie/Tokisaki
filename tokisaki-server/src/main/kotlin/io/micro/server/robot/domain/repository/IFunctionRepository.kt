package io.micro.server.robot.domain.repository

import io.micro.server.robot.domain.model.entity.FunctionDO
import io.smallrye.mutiny.Uni

interface IFunctionRepository {

    fun findAllFunctions(): Uni<List<FunctionDO>>

    fun findById(id: Long): Uni<FunctionDO>
}