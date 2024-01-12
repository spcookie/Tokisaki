package io.micro.server.dict.infra.dao

import io.micro.server.dict.infra.po.SystemDictEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository

interface ISystemDictDAO : PanacheRepository<SystemDictEntity> {
}