package io.micro.server.dict.infra.dao

import io.micro.server.dict.infra.po.SystemDictEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni

interface ISystemDictDAO : PanacheRepository<SystemDictEntity> {

    fun selectByKey(key: String): Uni<SystemDictEntity>

}