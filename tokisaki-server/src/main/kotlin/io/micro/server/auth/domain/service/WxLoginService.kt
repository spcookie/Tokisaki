package io.micro.server.auth.domain.service

import io.micro.server.auth.domain.model.entity.WXLoginUser
import io.micro.server.auth.domain.model.valobj.WXMessage
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink

/**
 *@author Augenstern
 *@since 2023/11/25
 */
interface WxLoginService {
    fun loginSubscript(driveId: String, sse: Sse, sink: SseEventSink): Multi<OutboundSseEvent>
    fun login(wxMessage: WXMessage, sse: Sse): Uni<WXLoginUser>
}