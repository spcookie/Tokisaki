package io.micro.server.dict.domain.repository

import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni

interface ISystemDictRepository {

    fun findSystemDictPage(page: Page): Uni<List<SystemDictDO>>

    fun countSystemDictPage(): Uni<Long>

    fun findById(id: Long): Uni<SystemDictDO>

    fun updateById(systemDictDO: SystemDictDO): Uni<SystemDictDO>

    fun save(systemDictDO: SystemDictDO): Uni<SystemDictDO>

}