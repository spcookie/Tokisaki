package io.micro.core.aop

import io.micro.core.annotation.InitAuthContext
import io.micro.core.context.AuthContext
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.annotation.Priority
import jakarta.interceptor.AroundInvoke
import jakarta.interceptor.Interceptor
import jakarta.interceptor.InvocationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@InitAuthContext
@Priority(99)
@Interceptor
class OnlineUserCountInterceptor(private val ds: ReactiveRedisDataSource) {

    companion object {
        const val ONLINE_USERS = "ONLINE:USERS"
        private val scope = CoroutineScope(SupervisorJob())
    }

    @AroundInvoke
    fun loginCount(ctx: InvocationContext): Any {
        val id = AuthContext.id
        scope.launch {
            ds.hash(Long::class.java).hset(ONLINE_USERS, id, System.currentTimeMillis()).awaitSuspending()
        }
        return ctx.proceed()
    }

}