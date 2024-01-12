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

    override fun countSystemDictPage(page: Page): Uni<Long> {
        return systemDictDAO.findAll().count()
    }

}