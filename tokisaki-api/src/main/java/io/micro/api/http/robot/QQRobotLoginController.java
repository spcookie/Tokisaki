package io.micro.api.http.robot;

import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.R;
import io.micro.server.robot.domain.service.RobotManagerService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestStreamElementType;

@Tag(name = "QQ机器人")
@InitAuthContext
@Path("/robot/qq")
public class QQRobotLoginController {

    @Inject
    public RobotManagerService robotManagerService;

    @Operation(summary = "QQ机器人扫码登录")
    @GET
    @Path("/login/{code}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    public Multi<OutboundSseEvent> loginQQRobot(
            @Parameter(description = "登录码") @PathParam("code") @Valid @NotNull String code,
            @Context Sse sse,
            @Context SseEventSink sink) {
        return robotManagerService.loginQQRobot(code, sse, sink);
    }

    @Operation(summary = "获取QQ机器人登录码")
    @GET
    @Path("/login/code/{id}")
    @Authenticated
    public Uni<R<String>> loginCode(@Parameter(description = "机器人ID") @PathParam("id") @Valid @NotNull Long id) {
        return robotManagerService.getLoginCode(id).map(R::newInstance);
    }

}