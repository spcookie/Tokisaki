package io.micro.core.aop

import io.micro.core.annotation.InitAuthContext
import io.micro.core.context.AuthContext
import io.quarkus.logging.Log
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.common.vertx.ContextLocals
import io.smallrye.mutiny.Uni
import jakarta.annotation.Priority
import jakarta.interceptor.AroundInvoke
import jakarta.interceptor.Interceptor
import jakarta.interceptor.InvocationContext
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders

/**
 *@author Augenstern
 *@since 2023/10/16
 */
@InitAuthContext
@Priority(9)
@Interceptor
class AuthContextInterceptor {

    @Context
    lateinit var headers: HttpHeaders

    private fun client(): String {
        return CurrentIdentityAssociation
            .current()
            .principal.name
            .split("@")[1]
    }

    private fun tenant(): String {
        return CurrentIdentityAssociation
            .current()
            .principal.name
            .split("@")[0]
    }

    private fun roles(): Set<String> {
        return CurrentIdentityAssociation.current().roles
    }

    private fun user(): String {
        return headers.getHeaderString("User")
    }

    @AroundInvoke
    fun init(ctx: InvocationContext): Any {
        Log.debug("AuthContext拦截器开始...")
        ContextLocals.put(AuthContext.Constant.USER, user())
        ContextLocals.put(AuthContext.Constant.TENANT, tenant())
        ContextLocals.put(AuthContext.Constant.CLIENT, client())
        ContextLocals.put(AuthContext.Constant.ROLES, roles())
        val proceed = ctx.proceed()
        if (proceed !is Uni<*>) {
            throw IllegalCallerException("@InitAuthContext注解只能在返回Uni的方法上使用")
        } else {
            return proceed.onTermination().invoke { _, _, _ ->
                ContextLocals.remove(AuthContext.Constant.USER)
                ContextLocals.remove(AuthContext.Constant.TENANT)
                ContextLocals.remove(AuthContext.Constant.CLIENT)
                ContextLocals.remove(AuthContext.Constant.ROLES)
            }
        }
    }
}