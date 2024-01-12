package io.micro.server.dict.domain.repository

import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface ISystemDictRepository {

    fun findSystemDictPage(page: Page): Uni<List<SystemDictDO>>

    fun countSystemDictPage(page: Page): Uni<Long>

}