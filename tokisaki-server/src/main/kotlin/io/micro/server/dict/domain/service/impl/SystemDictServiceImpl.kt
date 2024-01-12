package io.micro.server.dict.domain.service.impl

import io.micro.core.rest.Pageable
import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.micro.server.dict.domain.repository.ISystemDictRepository
import io.micro.server.dict.domain.service.SystemDictService
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemDictServiceImpl(private val systemDictRepository: ISystemDictRepository) : SystemDictService {

    @WithSession
    override fun findDictPage(pageable: Pageable): Uni<Pair<List<SystemDictDO>, Long>> {
        val page = Page.of(pageable.current, pageable.limit)
        return systemDictRepository.findSystemDictPage(page).flatMap { list ->
            systemDictRepository.countSystemDictPage(page).map { list to it }
        }
    }

}