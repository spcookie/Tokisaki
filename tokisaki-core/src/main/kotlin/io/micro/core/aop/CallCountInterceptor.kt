package io.micro.core.aop

import io.micro.core.annotation.CallCount
import io.micro.core.function.sdk.CommandService
import io.quarkus.logging.Log
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import jakarta.annotation.Priority
import jakarta.interceptor.AroundInvoke
import jakarta.interceptor.Interceptor
import jakarta.interceptor.InvocationContext
import java.util.function.Supplier

/**
 *@author Augenstern
 *@since 2023/10/15
 */
@CallCount
@Priority(99)
@Interceptor
class CallCountInterceptor(private val ds: ReactiveRedisDataSource) {

    companion object {
        private const val ALL = "statistic:cmd:all"
    }

    @AroundInvoke
    fun invoke(ctx: InvocationContext): Any {
        Log.debug("Cmd调用计数拦截器开始...")
        val target = ctx.target
        if (target is CommandService) {
            val proceed = ctx.proceed()
            if (proceed is Uni<*>) {
                return proceed.call(Supplier { incrCallCount(target.cmd().cmd) })
            } else {
                throw IllegalCallerException("@CallCount注解只能在返回Uni的方法上使用")
            }
        } else {
            throw IllegalCallerException("@CallCount注解只能在CommandService类中使用")
        }

    }

    fun fetchCallStatistic(key: String): Uni<Long> {
        return ds.value(Long::class.java)["$ALL:$key"]
    }

    fun incrCallCount(key: String): Uni<Long> {
        return ds.value(Long::class.java).incr("$ALL:$key")
    }
}