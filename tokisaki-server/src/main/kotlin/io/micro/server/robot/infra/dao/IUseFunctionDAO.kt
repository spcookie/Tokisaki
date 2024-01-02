package io.micro.server.robot.infra.dao

import io.micro.server.robot.infra.po.UseFunctionEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository

interface IUseFunctionDAO: PanacheRepository<UseFunctionEntity> {
}