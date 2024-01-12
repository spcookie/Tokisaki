package io.micro.server.dict.infra.dao.impl

import io.micro.server.dict.infra.dao.ISystemDictDAO
import io.micro.server.dict.infra.po.SystemDictEntity
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemDictDAO : ISystemDictDAO {

    override fun selectByKey(key: String): Uni<SystemDictEntity> {
        return find("key = ?1", key).singleResult()
    }

}