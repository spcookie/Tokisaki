package io.micro.server.robot.infra.dao

import io.micro.server.robot.infra.po.FunctionEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository

interface IFunctionDAO : PanacheRepository<FunctionEntity>