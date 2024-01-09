package io.micro.server.robot.domain.service

import io.micro.server.robot.domain.model.entity.FunctionDO
import io.smallrye.mutiny.Uni

interface FunctionService {

    fun getUserFunctions(): Uni<List<FunctionDO>>

    fun getFunctionById(id: Long): Uni<FunctionDO>

}