package io.micro.server.dict.infra.dao

import io.micro.server.dict.infra.po.SystemDictEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface ISystemDictDAO : PanacheRepository<SystemDictEntity> {

    fun selectByKey(key: String): Uni<SystemDictEntity>

    fun selectByKeyLike(key: String, page: Page): Uni<List<SystemDictEntity>>

    fun countByKeyLike(key: String): Uni<Long>

}