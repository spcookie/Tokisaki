package io.micro.api.wx

import io.micro.api.wx.convert.WxMessageConvert
import io.micro.api.wx.dto.AccessInfoDTO
import io.micro.api.wx.dto.ReplyMessageDTO
import io.micro.api.wx.dto.WxMessageDTO
import io.micro.core.annotation.InitAuthContext
import io.micro.core.filter.ReqInfo
import io.micro.server.auth.domain.service.WxLoginService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
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
@InitAuthContext
@Path("/wx")
class WxOfficialController {

    @Inject
    lateinit var wxLoginService: WxLoginService

    @Inject
    lateinit var reqInfo: ReqInfo

    @Inject
    lateinit var wxMessageConvert: WxMessageConvert

    @Context
    lateinit var sse: Sse

    @Operation(summary = "微信公众号认证回调", description = "认证成功后需要返回收到的echostr")
    @GET
    @Path("/callback")
    fun echo(@Parameter(description = "回调参数") @BeanParam accessInfoDTO: AccessInfoDTO): Uni<String> {
        return Uni.createFrom().item(accessInfoDTO.echostr)
    }

    @Operation(summary = "微信公众号接收信息回调")
    @POST
    @Path("/callback")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun message(@Parameter(description = "普通文本信息") wxMessageDTO: WxMessageDTO): Uni<ReplyMessageDTO> {
        return wxLoginService.login(wxMessageConvert.wxMessageDTO2WxMessage(wxMessageDTO), sse)
            .onItem().ifNotNull()
            .transform { user ->
                ReplyMessageDTO().apply {
                    fromUserName = wxMessageDTO.toUserName
                    toUserName = wxMessageDTO.fromUserName
                    createTime = System.currentTimeMillis().toString()
                    msgType = "text"
                    content = "登陆成功，${user.name}"
                }
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