package io.micro.server.function.infra.dao

import io.micro.server.function.infra.po.FunctionEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository

interface IFunctionDAO : PanacheRepository<FunctionEntity>