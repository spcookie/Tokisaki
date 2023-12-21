package io.micro.api.robot

import io.micro.api.robot.converter.RobotManagerConverter
import io.micro.api.robot.dto.RobotManagerDTO
import io.micro.core.annotation.InitAuthContext
import io.micro.core.rest.R
import io.micro.server.robot.domain.service.RobotManagerService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.*
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
@InitAuthContext
@Path("/robot/manager")
class RobotManagerController(
    private val robotManagerService: RobotManagerService,
    private val robotManagerConverter: RobotManagerConverter
) {
    @Operation(summary = "QQ机器人扫码登录")
    @GET
    @Path("login/qq/{qq}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    fun qqRobotLogin(
        @Parameter(description = "QQ账号") @PathParam("qq") qq: String,
        @Context sse: Sse
    ): Multi<OutboundSseEvent> {
        return robotManagerService.qrQQLogin(qq, sse)
    }

    @Operation(summary = "机器人创建")
    @POST
    @Path("/create")
    fun qqRobotCreate(@Parameter(description = "创建信息") robotManagerDTO: RobotManagerDTO): Uni<R<Unit>> {
        return robotManagerService.createRobot(robotManagerConverter.robotManagerDTO2RobotManager(robotManagerDTO))
            .replaceWith { R.newInstance("创建成功") }
    }
}