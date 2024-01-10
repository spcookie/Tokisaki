package io.micro.core.aop

import io.micro.core.annotation.CallCount
import io.micro.core.function.sdk.CommandService
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.annotation.Priority
import jakarta.inject.Inject
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
class CallCountInterceptor {

    @Inject
    lateinit var commandCache: CommandCache

    @AroundInvoke
    fun invoke(ctx: InvocationContext): Any {
        Log.debug("Cmd调用计数拦截器开始...")
        val target = ctx.target
        if (target is CommandService) {
            val proceed = ctx.proceed()
            if (proceed is Uni<*>) {
                return proceed.call(Supplier { commandCache.IncrCallCount(target.cmd().code) })
            } else {
                throw IllegalCallerException("@CallCount注解只能在返回Uni的方法上使用")
            }
        } else {
            throw IllegalCallerException("@CallCount注解只能在CommandService类中使用")
        }

    }
}