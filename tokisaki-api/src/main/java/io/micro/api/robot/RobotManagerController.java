package io.micro.api.robot;

import io.micro.api.robot.converter.RobotManagerConverter;
import io.micro.api.robot.dto.OperateFeatureFunctionDTO;
import io.micro.api.robot.dto.OperateRobotDTO;
import io.micro.api.robot.dto.QueryRobotDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.CommonCode;
import io.micro.core.rest.PageDTO;
import io.micro.core.rest.Pageable;
import io.micro.core.rest.R;
import io.micro.core.valid.ValidGroup;
import io.micro.server.robot.domain.model.entity.RobotDO;
import io.micro.server.robot.domain.model.valobj.FeatureFunction;
import io.micro.server.robot.domain.service.RobotManagerService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.OutboundSseEvent;
import jakarta.ws.rs.sse.Sse;
import kotlin.Unit;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestStreamElementType;

/**
 * @author Augenstern
 * @since 2023/11/23
 */
@Tag(name = "机器人操作")
@InitAuthContext
@Path("/robot/manager")
public class RobotManagerController {

    @Inject
    public RobotManagerService robotManagerService;

    @Inject
    public RobotManagerConverter robotManagerConverter;

    @Operation(summary = "QQ机器人扫码登录")
    @GET
    @Path("login/qq/{id}")
    @Authenticated
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.TEXT_HTML)
    public Multi<OutboundSseEvent> loginQQRobot(
            @Parameter(description = "机器人ID") @PathParam("id") Long id,
            @Context Sse sse) {
        return robotManagerService.loginQQRobot(id, sse);
    }

    @Operation(summary = "机器人查询")
    @POST
    @Path("/example")
    @Authenticated
    public Uni<R<PageDTO<QueryRobotDTO>>> getRobot(
            @Parameter(description = "查询样例") OperateRobotDTO operateRobotDTO,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("page") @DefaultValue("1") int page) {
        RobotDO robotManager = robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO);
        return robotManagerService.getRobotList(robotManager, Pageable.Companion.of(page, size))
                .map(robotDOPage -> R.Companion.newInstance(
                        CommonCode.OK.getMsg(),
                        PageDTO.Companion.of(
                                robotDOPage.getCurrent(),
                                robotDOPage.getLimit(),
                                robotDOPage.getRecords()
                                        .stream()
                                        .map(robotDO -> robotManagerConverter.robotManager2QueryRobotDTO(robotDO))
                                        .toList())
                ));
    }

    @Operation(summary = "机器人创建")
    @POST
    @Path("/")
    @Authenticated
    public Uni<R<QueryRobotDTO>> createRobot(@Parameter(description = "创建信息") @Valid @ConvertGroup(to = ValidGroup.Create.class) OperateRobotDTO operateRobotDTO) {
        operateRobotDTO.setState(0);
        return robotManagerService.createRobot(robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO))
                .map(it -> R.Companion.newInstance("创建成功", robotManagerConverter.robotManager2QueryRobotDTO(it)));
    }

    @Operation(summary = "机器人关闭")
    @PATCH
    @Path("/{id}")
    @Authenticated
    public Uni<R<Unit>> closeRobot(@Parameter(description = "机器人ID") @PathParam("id") Long id) {
        return robotManagerService.closeRobot(id).map(it -> R.Companion.newInstance());
    }

    @Operation(summary = "修改机器人信息")
    @PUT
    @Path("/")
    @Authenticated
    public Uni<R<QueryRobotDTO>> modifyRobotInfo(
            @Parameter(description = "机器人信息")
            @Valid
            @ConvertGroup(to = ValidGroup.Update.class)
            OperateRobotDTO operateRobotDTO) {
        RobotDO robotDO = robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO);
        return robotManagerService.modifyRobotInfo(robotDO)
                .map(robotManagerConverter::robotManager2QueryRobotDTO)
                .map(it -> R.Companion.newInstance(CommonCode.OK.getMsg(), it));
    }

    @Operation(summary = "添加机器人功能")
    @POST
    @Path("/{id}/function")
    @Authenticated
    public Uni<R<Unit>> addUseFunction(
            @Parameter(description = "机器人ID") @PathParam("id") Long robotId,
            @Parameter(description = "添加功能信息") OperateFeatureFunctionDTO operateFeatureFunctionDTO) {
        return robotManagerService.addFeatureFunction(
                        robotId,
                        robotManagerConverter.operateFeatureFunctionDTO2FeatureFunction(operateFeatureFunctionDTO))
                .map((it) -> R.Companion.newInstance());
    }


    @Operation(summary = "修改机器人功能")
    @PUT
    @Path("/{id}/function")
    @Authenticated
    public Uni<R<Unit>> modifyUseFunction(
            @Parameter(description = "机器人ID") @PathParam("id") Long robotId,
            @Parameter(description = "修改功能信息")
            @Valid
            @ConvertGroup(to = ValidGroup.Update.class)
            OperateFeatureFunctionDTO featureFunctionDTO) {
        FeatureFunction featureFunction = robotManagerConverter.operateFeatureFunctionDTO2FeatureFunction(featureFunctionDTO);
        return robotManagerService.modifyFeatureFunction(robotId, featureFunction).map((it) -> R.Companion.newInstance());
    }

}