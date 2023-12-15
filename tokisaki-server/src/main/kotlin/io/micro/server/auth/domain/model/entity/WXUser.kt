package io.micro.server.auth.domain.model.entity

import io.micro.core.auth.GenerateToken
import io.micro.core.auth.LoginCache
import io.micro.core.entity.BaseDomainEntity
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class WXUser(
    var name: String,
    var openid: String,
    var authorities: MutableSet<String> = mutableSetOf()
) : BaseDomainEntity() {

    companion object {
        private val loginCache = LoginCache.newInstance()

        fun loginStart(driveId: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
            val id = loginCache.idPool[driveId]
            loginCache.linkPool.put(id, sink)
            val broadcaster = sse.newBroadcaster()
            broadcaster.register(sink)
            broadcaster.onClose {
                loginCache.idPool.invalidate(driveId)
            }
            return Multi.createFrom().emitter { em ->
                em.emit(sse.newEvent("start#$id"))
            }
        }

        fun loginEnd(code: String, token: String, sse: Sse) {
            loginCache.linkPool.getIfPresent(code)?.send(sse.newEvent("end#$token"))
            loginCache.linkPool.invalidate(code)
        }

        fun containsCode(code: String): Boolean {
            return loginCache.linkPool.getIfPresent(code) != null
        }

        fun defaultUser(openId: String): WXUser {
            return WXUser(randomName(), openId)
        }

        private fun randomName(): String {
            // TODO
            return "cokie"
        }
    }

    fun generateToken(): String {
        return GenerateToken.generate(name, openid, authorities)
    }
}