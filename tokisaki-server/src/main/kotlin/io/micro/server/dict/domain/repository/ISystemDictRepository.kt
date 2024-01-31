package io.micro.server.dict.domain.repository

import io.micro.core.rest.Pageable
import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.smallrye.mutiny.Uni

interface ISystemDictRepository {

    fun findSystemDictByKeyLike(keyword: String, pageable: Pageable): Uni<List<SystemDictDO>>

    fun findSystemDict(pageable: Pageable): Uni<List<SystemDictDO>>

    fun countSystemDict(): Uni<Long>

    fun countSystemDictByKeyLike(keyword: String): Uni<Long>

    fun findById(id: Long): Uni<SystemDictDO>

    fun updateById(systemDictDO: SystemDictDO): Uni<SystemDictDO>

    fun save(systemDictDO: SystemDictDO): Uni<SystemDictDO>

    fun findByKey(key: String): Uni<SystemDictDO>

}