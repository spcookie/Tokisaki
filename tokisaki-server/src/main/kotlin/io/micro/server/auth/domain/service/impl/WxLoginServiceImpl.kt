package io.micro.server.auth.domain.service.impl

import io.micro.server.auth.domain.model.entity.WXLoginUser
import io.micro.server.auth.domain.model.valobj.WXMessage
import io.micro.server.auth.domain.service.WxLoginService
import io.micro.server.auth.infra.repository.AuthRepository
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
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
        return WXLoginUser.loginStart(driveId, sse, sink)
    }

    @WithTransaction
    override fun login(wxMessage: WXMessage, sse: Sse): Uni<WXLoginUser> {
        val code = wxMessage.content
        val openId = wxMessage.fromUserName
        return if (WXLoginUser.containsCode(code)) {
            loginUser(openId).invoke { user -> WXLoginUser.loginEnd(code, user.token, sse) }
        } else {
            Uni.createFrom().nullItem()
        }
    }

    private fun loginUser(openId: String): Uni<WXLoginUser> {
        return authRepository.findUserByOpenid(openId)
            .flatMap { user ->
                if (user == null) {
                    val defaultUser = WXLoginUser.defaultUser(openId)
                    authRepository.registerUser(defaultUser).replaceWith(defaultUser)
                } else {
                    Uni.createFrom().item(user)
                }
            }
            .map {
                it.apply {
                    token = it.generateToken()
                }
            }
    }
}