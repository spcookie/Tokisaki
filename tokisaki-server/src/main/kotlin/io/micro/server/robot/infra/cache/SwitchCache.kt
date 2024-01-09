package io.micro.server.robot.infra.cache

import io.micro.server.robot.infra.cache.dto.SwitchDTO
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SwitchCache(private val ds: ReactiveRedisDataSource) {

    companion object {
        private const val KEY = "switch:"
    }

    fun loadSwitchConfig(id: Long): Uni<SwitchDTO> {
        return ds.value(SwitchDTO::class.java).get(KEY + id)
            .onItem().ifNull().continueWith(SwitchDTO())
    }

    fun setSwitchConfig(id: Long, switch: SwitchDTO): Uni<Unit> {
        return ds.value(SwitchDTO::class.java)
            .set(KEY + id, switch)
            .replaceWithUnit()
    }

}