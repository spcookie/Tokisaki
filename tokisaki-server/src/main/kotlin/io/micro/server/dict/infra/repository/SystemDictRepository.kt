package io.micro.server.dict.infra.repository

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

    override fun findSystemDictPage(page: Page): Uni<List<SystemDictDO>> {
        return systemDictDAO.findAll()
            .page(page)
            .list()
            .map { it.map { systemDictConverter.systemDictEntity2SystemDictDO(it)!! } }
    }

    override fun countSystemDictPage(): Uni<Long> {
        return systemDictDAO.findAll().count()
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
        return systemDictDAO.persist(systemDictConverter.systemDictDO2SystemDictEntity(systemDictDO.also {
            it.id = null
        }))
            .map(systemDictConverter::systemDictEntity2SystemDictDO)
    }

}