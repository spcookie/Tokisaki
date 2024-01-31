package io.micro.server.dict.infra.repository

import io.micro.core.rest.Pageable
import io.micro.core.util.converter
import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.micro.server.dict.domain.repository.ISystemDictRepository
import io.micro.server.dict.infra.converter.SystemDictConverter
import io.micro.server.dict.infra.dao.ISystemDictDAO
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemDictRepository(
    private val systemDictDAO: ISystemDictDAO,
    private val systemDictConverter: SystemDictConverter
) : ISystemDictRepository {

    override fun findSystemDictByKeyLike(keyword: String, pageable: Pageable): Uni<List<SystemDictDO>> {
        val page = Page.of(pageable.current - 1, pageable.limit)
        return systemDictDAO.selectByKeyLike(keyword, page)
            .map { it.converter(systemDictConverter::systemDictEntity2SystemDictDO) }
    }

    override fun findSystemDict(pageable: Pageable): Uni<List<SystemDictDO>> {
        val page = Page.of(pageable.current - 1, pageable.limit)
        return systemDictDAO.findAll()
            .page(page).list()
            .map { it.converter(systemDictConverter::systemDictEntity2SystemDictDO) }
    }

    override fun countSystemDict(): Uni<Long> {
        return systemDictDAO.findAll().count()
    }

    override fun countSystemDictByKeyLike(keyword: String): Uni<Long> {
        return systemDictDAO.countByKeyLike(keyword)
    }

    override fun findById(id: Long): Uni<SystemDictDO> {
        return systemDictDAO.findById(id).map(systemDictConverter::systemDictEntity2SystemDictDO)
    }

    override fun updateById(systemDictDO: SystemDictDO): Uni<SystemDictDO> {
        val id = systemDictDO.id
        return if (id == null) {
            Uni.createFrom().item(systemDictDO)
        } else {
            systemDictDAO.findById(id)
                .invoke { entity ->
                    systemDictConverter.systemDictDO2SystemDictEntity(systemDictDO, entity)
                }.replaceWith(systemDictDO)
        }
    }

    override fun save(systemDictDO: SystemDictDO): Uni<SystemDictDO> {
        return systemDictDAO.persist(
            systemDictConverter.systemDictDO2SystemDictEntity(systemDictDO.also { it.id = null })
        )
            .map(systemDictConverter::systemDictEntity2SystemDictDO)
    }

    override fun findByKey(key: String): Uni<SystemDictDO> {
        return systemDictDAO.selectByKey(key)
            .map(systemDictConverter::systemDictEntity2SystemDictDO)
    }

}