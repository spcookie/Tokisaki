package io.micro.api.http.robot;

import io.micro.api.robot.converter.RobotManagerConverter;
import io.micro.api.robot.converter.SwitchConverter;
import io.micro.api.robot.dto.*;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.PageDTO;
import io.micro.core.rest.Pageable;
import io.micro.core.rest.R;
import io.micro.core.valid.ValidGroup;
import io.micro.server.robot.domain.model.entity.FeatureFunctionDO;
import io.micro.server.robot.domain.model.entity.RobotDO;
import io.micro.server.robot.domain.service.RobotManagerService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import kotlin.Unit;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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

    @Inject
    public SwitchConverter switchConverter;

    @Operation(summary = "查询机器人")
    @POST
    @Path("/example")
    @Authenticated
    public Uni<R<PageDTO<QueryRobotDTO>>> getRobot(
            @Parameter(description = "查询样例") @Valid OperateRobotDTO operateRobotDTO,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("page") @DefaultValue("1") int page) {
        RobotDO robotManager = robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO);
        return robotManagerService.getRobotList(robotManager, Pageable.of(page, size))
                .map(it -> PageDTO.converter(it, robotManagerConverter::robotManager2QueryRobotDTO))
                .map(R::newInstance);
    }

    @Operation(summary = "创建机器人")
    @POST
    @Path("/")
    @Authenticated
    public Uni<R<QueryRobotDTO>> createRobot(@Parameter(description = "创建信息") @Valid @ConvertGroup(to = ValidGroup.Create.class) OperateRobotDTO operateRobotDTO) {
        return robotManagerService.createRobot(robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO))
                .map(it -> R.newInstance(robotManagerConverter.robotManager2QueryRobotDTO(it)));
    }

    @Operation(summary = "关闭机器人")
    @PATCH
    @Path("/{id}")
    @Authenticated
    public Uni<R<Unit>> closeRobot(@Parameter(description = "机器人ID") @PathParam("id") @Valid @NotNull Long id) {
        return robotManagerService.closeRobot(id).map(it -> R.newInstance());
    }

    @Operation(summary = "修改机器人信息")
    @PUT
    @Path("/")
    @Authenticated
    public Uni<R<QueryRobotDTO>> modifyRobotInfo(
            @Parameter(description = "机器人信息") @Valid @ConvertGroup(to = ValidGroup.Update.class) OperateRobotDTO operateRobotDTO) {
        RobotDO robotDO = robotManagerConverter.operateRobotDTO2RobotManager(operateRobotDTO);
        return robotManagerService.modifyRobotInfo(robotDO)
                .map(dto -> robotManagerConverter.robotManager2QueryRobotDTO(dto))
                .map(R::newInstance);
    }

    @Operation(summary = "添加机器人功能")
    @POST
    @Path("/{id}/function")
    @Authenticated
    public Uni<R<Unit>> addUseFunction(
            @Parameter(description = "机器人ID") @PathParam("id") @Valid @NotNull Long robotId,
            @Parameter(description = "添加功能信息") OperateFeatureFunctionDTO operateFeatureFunctionDTO) {
        return robotManagerService.addFeatureFunction(
                        robotId,
                        robotManagerConverter.operateFeatureFunctionDTO2FeatureFunction(operateFeatureFunctionDTO))
                .map(it -> R.newInstance());
    }

    @Operation(summary = "修改机器人功能")
    @PUT
    @Path("/{id}/function")
    @Authenticated
    public Uni<R<Unit>> modifyUseFunction(
            @Parameter(description = "机器人ID") @PathParam("id") @Valid @NotNull Long robotId,
            @Parameter(description = "修改功能信息") @Valid @ConvertGroup(to = ValidGroup.Update.class) OperateFeatureFunctionDTO featureFunctionDTO) {
        FeatureFunctionDO featureFunctionDO = robotManagerConverter.operateFeatureFunctionDTO2FeatureFunction(featureFunctionDTO);
        return robotManagerService.modifyFeatureFunction(robotId, featureFunctionDO).map((it) -> R.newInstance());
    }

    @Operation(summary = "保存或修改机器人功能权限开关")
    @POST
    @Path("/function/{id}")
    @Authenticated
    public Uni<R<Void>> saveOrUpdateFunctionSwitch(
            @Parameter(description = "机器人功能权限ID") @PathParam("id") @Valid @NotNull Long id,
            QuerySwitchDTO querySwitchDTO) {
        return robotManagerService.addOrModifyFunctionSwitch(id, switchConverter.switchDTO2switch(querySwitchDTO))
                .map(it -> R.newInstance());
    }

    @Operation(summary = "查询机器人功能权限开关")
    @GET
    @Path("/function/{id}")
    @Authenticated
    public Uni<R<QuerySwitchDTO>> getFunctionSwitch(@Parameter(description = "机器人功能权限ID") @PathParam("id") @Valid @NotNull Long id) {
        return robotManagerService.getFunctionSwitch(id)
                .map(switchConverter::switch2SwitchDTO)
                .map(R::newInstance);
    }

    @Operation(summary = "查询机器人联系人")
    @GET
    @Path("/{id}/contact")
    @Authenticated
    public Uni<R<QueryRobotContactsDTO>> loadRobotContacts(@Parameter(description = "机器人ID") @PathParam("id") @Valid @NotNull Long id) {
        return robotManagerService.loadContacts(id)
                .map(robotManagerConverter::robotContact2QueryRobotContactDTO)
                .map(R::newInstance);
    }

}