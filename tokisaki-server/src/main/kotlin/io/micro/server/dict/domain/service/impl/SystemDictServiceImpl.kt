package io.micro.server.dict.domain.service.impl

import io.micro.core.rest.Pageable
import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.micro.server.dict.domain.repository.ISystemDictRepository
import io.micro.server.dict.domain.service.SystemDictService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemDictServiceImpl(private val systemDictRepository: ISystemDictRepository) : SystemDictService {

    @WithSession
    override fun findDictPage(keyword: String?, pageable: Pageable): Uni<Pair<List<SystemDictDO>, Long>> {
        return if (!keyword.isNullOrBlank()) {
            systemDictRepository.findSystemDictByKeyLike(keyword, pageable).flatMap { list ->
                systemDictRepository.countSystemDictByKeyLike(keyword).map { list to it }
            }
        } else {
            systemDictRepository.findSystemDict(pageable).flatMap { list ->
                systemDictRepository.countSystemDict().map { list to it }
            }
        }
    }

    @WithTransaction
    @WithSession
    override fun saveOrUpdateDict(systemDictDO: SystemDictDO): Uni<SystemDictDO> {
        val id = systemDictDO.id
        return if (id != null) {
            systemDictRepository.findById(id).flatMap {
                systemDictRepository.updateById(systemDictDO)
            }
        } else {
            systemDictRepository.save(systemDictDO)
        }
    }

    @WithSession
    override fun findDictByKey(key: String): Uni<SystemDictDO> {
        return systemDictRepository.findByKey(key)
    }

}