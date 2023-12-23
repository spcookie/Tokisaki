package io.micro.core.aop

import io.micro.core.annotation.InitAuthContext
import io.micro.core.context.AuthContext
import io.quarkus.logging.Log
import io.quarkus.security.identity.CurrentIdentityAssociation
import io.smallrye.common.vertx.ContextLocals
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.annotation.Priority
import jakarta.interceptor.AroundInvoke
import jakarta.interceptor.Interceptor
import jakarta.interceptor.InvocationContext
import jakarta.ws.rs.core.Context
import org.eclipse.microprofile.jwt.Claims
import org.eclipse.microprofile.jwt.JsonWebToken

/**
 *@author Augenstern
 *@since 2023/10/16
 */
@InitAuthContext
@Priority(9)
@Interceptor
class AuthContextInterceptor {

    @Context
    lateinit var jwt: JsonWebToken

    private fun openid(): String {
        return CurrentIdentityAssociation.current().principal.name
    }

    private fun roles(): Set<String> {
        return CurrentIdentityAssociation.current().roles
    }

    private fun nickname(): String {
        return jwt.getClaim(Claims.nickname) ?: ""
    }

    private fun id(): String {
        return jwt.getClaim(Claims.sub)
    }

    @AroundInvoke
    fun init(ctx: InvocationContext): Any {
        Log.debug("AuthContext拦截器开始...")
        poll()
        val proceed = ctx.proceed()
        return postInvokeMulti(postInvokeUni(proceed))
    }

    private fun poll() {
        ContextLocals.put(AuthContext.Constant.ID, id())
        ContextLocals.put(AuthContext.Constant.OPENID, openid())
        ContextLocals.put(AuthContext.Constant.NICKNAME, nickname())
        ContextLocals.put(AuthContext.Constant.ROLES, roles())
    }

    private fun remove() {
        ContextLocals.remove(AuthContext.Constant.OPENID)
        ContextLocals.remove(AuthContext.Constant.NICKNAME)
        ContextLocals.remove(AuthContext.Constant.ROLES)
    }

    private fun postInvokeUni(proceed: Any): Any {
        return if (proceed is Uni<*>) proceed.onTermination().invoke { _, _, _ -> remove() }
        else proceed
    }

    private fun postInvokeMulti(proceed: Any): Any {
        return if (proceed is Multi<*>) proceed.onTermination().invoke { _, _ -> remove() }
        else proceed
    }

}