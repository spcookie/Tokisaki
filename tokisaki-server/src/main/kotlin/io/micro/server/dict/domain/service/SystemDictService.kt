package io.micro.server.dict.domain.service

import io.micro.core.rest.Pageable
import io.micro.server.dict.domain.model.entity.SystemDictDO
import io.smallrye.mutiny.Uni

interface SystemDictService {

    fun findDictPage(pageable: Pageable): Uni<Pair<List<SystemDictDO>, Long>>

    fun saveOrUpdateDict(systemDictDO: SystemDictDO): Uni<SystemDictDO>

}