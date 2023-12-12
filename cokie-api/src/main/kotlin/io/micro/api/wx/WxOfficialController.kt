package io.micro.api.wx

import cn.hutool.core.util.StrUtil
import io.micro.api.wx.dto.AccessInfo
import io.micro.api.wx.dto.TextMessage
import io.micro.core.filter.ReqInfo
import io.micro.server.auth.domain.service.WxLoginService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import jakarta.ws.rs.sse.SseEventSink
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.jboss.resteasy.reactive.RestStreamElementType

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Path("/wx")
class WxOfficialController(
    private val wxLoginService: WxLoginService,
    private val reqInfo: ReqInfo
) {

    @Context
    lateinit var sse: Sse

    @Operation(summary = "微信公众号认证回调", description = "认证成功后需要返回收到的echostr")
    @GET
    @Path("/callback")
    fun echo(@Parameter(description = "回调参数") @BeanParam accessInfo: AccessInfo): Uni<String> {
        return Uni.createFrom().item(accessInfo.echostr)
    }

    @Operation(summary = "微信公众号接收信息回调")
    @POST
    @Path("/callback")
    @Consumes(MediaType.TEXT_XML)
    fun message(@Parameter(description = "普通文本信息") textMessage: TextMessage): Uni<Unit> {
        val code = textMessage.content
        val openid = textMessage.fromUserName
        return if (StrUtil.isNotBlank(code) && StrUtil.isNotBlank(openid)) {
            wxLoginService.login(code!!, openid!!, sse)
        } else {
            Uni.createFrom().nullItem()
        }
    }

    @Operation(summary = "微信公众号登录订阅")
    @GET
    @Path("/subscript")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    fun subscript(@Context sink: SseEventSink): Multi<OutboundSseEvent> {
        val driveId = reqInfo.driveId
        return if (driveId != null) {
            wxLoginService.loginSubscript(driveId, sse, sink)
        } else {
            Multi.createFrom().failure(RuntimeException("error"))
        }
    }
}