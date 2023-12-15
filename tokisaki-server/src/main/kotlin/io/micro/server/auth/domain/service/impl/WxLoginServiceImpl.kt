package io.micro.server.auth.domain.service.impl

import io.micro.server.auth.domain.model.entity.WXUser
import io.micro.server.auth.domain.service.WxLoginService
import io.micro.server.auth.infra.repository.AuthRepository
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.replaceWithUnit
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@ApplicationScoped
class WxLoginServiceImpl(
    private val authRepository: AuthRepository
) : WxLoginService {

    override fun loginSubscript(driveId: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent> {
        return WXUser.loginStart(driveId, sse, sink)
    }

    @WithTransaction
    override fun login(code: String, openId: String, sse: Sse): Uni<Unit> {
        return if (WXUser.containsCode(code)) {
            loginUser(openId).map { token ->
                WXUser.loginEnd(code, token, sse)
            }.replaceWithUnit()
        } else {
            Uni.createFrom().voidItem().replaceWithUnit()
        }
    }

    private fun loginUser(openId: String): Uni<String> {
        return authRepository.findUserByOpenid(openId).flatMap { user ->
            if (user == null) {
                val defaultUser = WXUser.defaultUser(openId)
                authRepository.registerUser(defaultUser)
                    .replaceWith(defaultUser)
            } else {
                Uni.createFrom().item(user)
            }
        }.map {
            it.generateToken()
        }
    }
}