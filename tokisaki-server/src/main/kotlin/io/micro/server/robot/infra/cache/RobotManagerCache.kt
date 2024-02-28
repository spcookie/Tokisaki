package io.micro.server.robot.infra.cache

import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class RobotManagerCache(private val ds: ReactiveRedisDataSource) {

    companion object {
        private const val LOGIN_CODE_KEY = "robot:manager:code:"
    }

    fun addLoginCode(code: String, id: Long): Uni<Unit> {
        return ds.value(Long::class.java).setex(LOGIN_CODE_KEY + code, 60, id).replaceWithUnit()
    }

    fun getLoginRobotId(code: String): Uni<Long> {
        return ds.value(Long::class.java)[LOGIN_CODE_KEY + code]
    }

}