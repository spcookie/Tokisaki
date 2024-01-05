package io.micro.server.robot.infra.dao

import io.micro.server.robot.infra.po.UseFunctionEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni

interface IUseFunctionDAO: PanacheRepository<UseFunctionEntity> {
    fun selectUseFunctionByRobotId(id: Long): Uni<List<UseFunctionEntity>>
}