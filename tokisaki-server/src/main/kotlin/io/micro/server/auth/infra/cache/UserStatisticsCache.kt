package io.micro.server.auth.infra.cache

import io.micro.core.aop.OnlineUserCountInterceptor
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserStatisticsCache(private val ds: ReactiveRedisDataSource) {

    fun findAllUserLastLoginTime(): Uni<Map<String, Long>> {
        return ds.hash(Long::class.java).hgetall(OnlineUserCountInterceptor.ONLINE_USERS)
    }

}