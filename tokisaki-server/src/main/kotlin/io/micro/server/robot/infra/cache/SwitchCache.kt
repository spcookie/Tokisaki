package io.micro.server.robot.infra.cache

import io.micro.server.robot.infra.cache.dto.SwitchDTO
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.json.Json

@ApplicationScoped
class SwitchCache(private val ds: ReactiveRedisDataSource, private val json: Json) {

    companion object {
        private const val KEY = "switch:"
    }

    fun loadSwitchConfig(id: Long): Uni<SwitchDTO> {
        return ds.value(SwitchDTO::class.java).get(KEY + id)
    }

    fun setSwitchConfig(id: Long, switch: SwitchDTO): Uni<Unit> {
        return ds.value(SwitchDTO::class.java)
            .set(id.toString(), switch)
            .replaceWithUnit()
    }

}