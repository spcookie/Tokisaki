package io.micro.server.dict.infra.dao.impl

import io.micro.server.dict.infra.dao.ISystemDictDAO
import io.micro.server.dict.infra.po.SystemDictEntity
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemDictDAO : ISystemDictDAO {

    override fun selectByKey(key: String): Uni<SystemDictEntity> {
        return find("key = ?1", key).singleResult()
    }

    override fun selectByKeyLike(key: String, page: Page): Uni<List<SystemDictEntity>> {
        return find("key like concat('%', ?1, '%')", key).page(page).list()
    }

    override fun countByKeyLike(key: String): Uni<Long> {
        return find("key like concat('%', ?1, '%')", key).count()
    }

}