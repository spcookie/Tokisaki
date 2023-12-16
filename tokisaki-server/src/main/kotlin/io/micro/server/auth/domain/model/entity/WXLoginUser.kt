package io.micro.server.auth.domain.model.entity

import io.micro.core.auth.GenerateJwtToken
import io.micro.core.auth.LoginLinkCache
import io.micro.core.entity.BaseDomainEntity
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class WXLoginUser(
    var name: String,
    var openid: String,
    var authorities: MutableSet<String> = mutableSetOf()
) : BaseDomainEntity() {

    companion object {
        private val loginLinkCache = LoginLinkCache.newInstance()

        fun loginStart(driveId: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
            val code = loginLinkCache.idPool[driveId]
            loginLinkCache.linkPool.put(code.value, sink)
            val broadcaster = sse.newBroadcaster()
            broadcaster.register(sink)
            broadcaster.onClose {
                loginLinkCache.idPool.invalidate(driveId)
                code.back()
            }
            return Multi.createFrom().emitter { em -> em.emit(sse.newEvent("start#${code.value}")) }
        }

        fun loginEnd(code: String, token: String, sse: Sse) {
            loginLinkCache.linkPool.getIfPresent(code)?.send(sse.newEvent("end#$token"))
            loginLinkCache.linkPool.invalidate(code)
        }

        fun containsCode(code: String): Boolean {
            return loginLinkCache.linkPool.getIfPresent(code) != null
        }

        fun defaultUser(openId: String): WXLoginUser {
            return WXLoginUser(randomName(), openId)
        }

        private fun randomName(): String {
            // TODO
            return "cokie"
        }
    }

    fun generateToken(): String {
        return GenerateJwtToken.generate(name, openid, authorities)
    }
}