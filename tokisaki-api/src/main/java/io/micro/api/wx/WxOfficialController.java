package io.micro.api.wx;

import io.micro.api.wx.convert.WxMessageConvert;
import io.micro.api.wx.dto.AccessInfoDTO;
import io.micro.api.wx.dto.ReplyMessageDTO;
import io.micro.api.wx.dto.WxMessageDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.filter.ReqInfo;
import io.micro.server.auth.domain.service.WxLoginService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestStreamElementType;

import static io.micro.core.exception.AssertKt.failWith;


/**
 * @author Augenstern
 * @since 2023/11/25
 */
@Tag(name = "微信公众号回调")
@InitAuthContext
@Path("/wx")
public class WxOfficialController {

    @Inject
    WxLoginService wxLoginService;

    @Inject
    ReqInfo reqInfo;

    @Inject
    WxMessageConvert wxMessageConvert;

    @Context
    Sse sse;

    @Operation(summary = "微信公众号认证回调", description = "认证成功后需要返回收到的echostr")
    @GET
    @Path("/callback")

    public Uni<String> echo(@Parameter(description = "回调参数") @BeanParam AccessInfoDTO accessInfoDTO) {
        return Uni.createFrom().item(accessInfoDTO.getEchostr());
    }

    @Operation(summary = "微信公众号接收信息回调")
    @POST
    @Path("/callback")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Uni<ReplyMessageDTO> message(@Parameter(description = "普通文本信息") WxMessageDTO wxMessageDTO) {
        return wxLoginService.login(wxMessageConvert.wxMessageDTO2WxMessage(wxMessageDTO), sse)
                .onItem().ifNotNull()
                .transform(user -> {
                    ReplyMessageDTO replyMessageDTO = new ReplyMessageDTO();
                    replyMessageDTO.setFromUserName(wxMessageDTO.getToUserName());
                    replyMessageDTO.setCreateTime(String.valueOf(System.currentTimeMillis()));
                    replyMessageDTO.setMsgType("text");
                    replyMessageDTO.setContent("登陆成功，" + user.getName());
                    return replyMessageDTO;
                });
    }

    @Operation(summary = "微信公众号登录订阅")
    @GET
    @Path("/subscript")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    public Multi<OutboundSseEvent> subscript(@Context SseEventSink sink) {
        String driveId = reqInfo.getDriveId();
        if (driveId != null) {
            return wxLoginService.loginSubscript(driveId, sse, sink);
        } else {
            return Multi.createFrom().failure(failWith("未找到设备ID"));
        }
    }
}