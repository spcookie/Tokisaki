package io.micro.server.auth.domain.model.entity

import io.micro.core.auth.GenerateJwtToken
import io.micro.core.auth.LoginLinkCache
import io.micro.core.base.BaseDomainEntity
import io.micro.core.exception.requireNonNull
import io.micro.server.auth.domain.model.valobj.DefaultName
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class WXLoginUserDO(
    var name: String,
    var openid: String,
    var authorities: MutableList<AuthorityDO>,
) : BaseDomainEntity() {

    lateinit var token: String

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
            loginLinkCache.linkPool.getIfPresent(code)?.apply {
                send(sse.newEvent("end#$token"))
                close()
            }
            loginLinkCache.linkPool.invalidate(code)
        }

        fun containsCode(code: String): Boolean {
            return loginLinkCache.linkPool.getIfPresent(code) != null
        }

        fun defaultUser(openId: String, authorities: MutableSet<String>): WXLoginUserDO {
            return WXLoginUserDO(
                DefaultName.generate(),
                openId,
                authorities.map { AuthorityDO().apply { value = it } }.toMutableList()
            )
        }

        fun refreshCode(driveId: String, sse: Sse) {
            val code = loginLinkCache.idPool[driveId]
            loginLinkCache.idPool.invalidate(driveId)
            val newCode = loginLinkCache.idPool[driveId]
            loginLinkCache.linkPool.getIfPresent(code.value)?.apply {
                loginLinkCache.linkPool.put(newCode.value, this)
                send(sse.newEvent("refresh#${newCode.value}"))
            }
            code.back()
        }
    }

    fun generateToken(): String {
        requireNonNull(id, "生成Jwt时失败，原因是：ID为空")
        return GenerateJwtToken.generate(name, id!!, openid, authorities.map { it.value }.filterNotNull().toSet())
    }
}