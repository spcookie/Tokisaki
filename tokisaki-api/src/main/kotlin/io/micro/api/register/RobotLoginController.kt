package io.micro.api.register

import io.micro.server.register.domain.service.RobotLoginService
import io.smallrye.mutiny.Multi
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.sse.OutboundSseEvent
import jakarta.ws.rs.sse.Sse
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.jboss.resteasy.reactive.RestStreamElementType

/**
 *@author Augenstern
 *@since 2023/11/23
 */
@Path("/robot/login")
class RobotLoginController(
    private val robotLoginService: RobotLoginService
) {
    @Operation(summary = "QQ机器人扫码登录")
    @GET
    @Path("qq/{qq}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    fun qqLogin(
        @Parameter(description = "QQ账号") @PathParam("qq") qq: String,
        @Context sse: Sse
    ): Multi<OutboundSseEvent> {
        return robotLoginService.qrQQLogin(qq, sse)
    }
}