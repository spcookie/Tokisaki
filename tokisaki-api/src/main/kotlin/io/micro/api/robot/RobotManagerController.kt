package io.micro.api.robot

import io.micro.api.robot.converter.RobotManagerConverter
import io.micro.api.robot.dto.RobotDTO
import io.micro.core.annotation.InitAuthContext
import io.micro.core.rest.PageDTO
import io.micro.core.rest.Pageable
import io.micro.core.rest.R
import io.micro.core.valid.ValidGroup
import io.micro.server.robot.domain.service.RobotManagerService
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.validation.groups.ConvertGroup
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
class RobotManagerController {

    @Inject
    lateinit var robotManagerService: RobotManagerService

    @Inject
    lateinit var robotManagerConverter: RobotManagerConverter

    @Operation(summary = "QQ机器人扫码登录")
    @GET
    @Path("login/qq/{id}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    fun loginQQRobot(
        @Parameter(description = "机器人ID") @PathParam("id") id: Long,
        @Context sse: Sse
    ): Multi<OutboundSseEvent> {
        return robotManagerService.loginQQRobot(id, sse)
    }

    @Operation(summary = "机器人查询")
    @POST
    @Path("/example")
    fun getRobot(
        @Parameter(description = "查询样例") robotDTO: RobotDTO,
        @QueryParam("size") @DefaultValue("10") size: Int,
        @QueryParam("page") @DefaultValue("1") page: Int
    ): Uni<R<PageDTO<RobotDTO>>> {
        val robotManager = robotManagerConverter.robotManagerDTO2RobotManager(robotDTO)
        return robotManagerService.getRobotList(robotManager, Pageable.of(page, size))
            .map {
                R.newInstance(
                    data = PageDTO.of(
                        it.current,
                        it.limit,
                        it.records.map { robotManagerConverter.robotManager2RobotManagerDTO(it) }
                    )
                )
            }
    }

    @Operation(summary = "机器人创建")
    @POST
    @Path("/")
    fun createRobot(@Parameter(description = "创建信息") @Valid @ConvertGroup(to = ValidGroup.Create::class) robotDTO: RobotDTO): Uni<R<RobotDTO>> {
        return robotManagerService.createRobot(robotManagerConverter.robotManagerDTO2RobotManager(robotDTO.also {
            it.state = 0
        }))
            .map { R.newInstance("创建成功", robotManagerConverter.robotManager2RobotManagerDTO(it)) }
    }

    @Operation(summary = "机器人关闭")
    @PATCH
    @Path("/{id}")
    fun closeRobot(@Parameter(description = "机器人ID") @PathParam("id") id: Long): Uni<R<Unit>> {
        return robotManagerService.closeRobot(id).map { R.newInstance() }
    }
}