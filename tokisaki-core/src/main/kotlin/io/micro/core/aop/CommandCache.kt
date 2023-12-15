package io.micro.core.aop

import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/10/8
 */
@ApplicationScoped
class CommandCache(private val ds: ReactiveRedisDataSource) {

    companion object {
        private const val KEY = "call:statistic"
    }

    fun fetchCallStatistic(key: String): Uni<Long> {
        return ds.value(Long::class.java)["$KEY:$key"]
    }

    fun IncrCallCount(key: String): Uni<Long> {
        return ds.value(Long::class.java).incr("$KEY:$key")
    }
}