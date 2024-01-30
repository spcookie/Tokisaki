package io.micro.api.http.user;

import io.micro.api.user.converter.UserConverter;
import io.micro.api.user.dto.DispatchAuthDTO;
import io.micro.api.user.dto.OperateUserDTO;
import io.micro.api.user.dto.QueryUserDTO;
import io.micro.api.user.dto.UserStatisticsDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.PageDTO;
import io.micro.core.rest.Pageable;
import io.micro.core.rest.R;
import io.micro.server.auth.domain.service.AuthService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import kotlin.Unit;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "用户管理")
@InitAuthContext
@Path("/user")
public class UserController {

    @Inject
    public AuthService authService;

    @Inject
    public UserConverter userConverter;

    @Operation(summary = "修改用户信息")
    @Authenticated
    @PATCH
    public Uni<R<QueryUserDTO>> modifyUserInfo(@Parameter(name = "用户信息") @Valid OperateUserDTO userDTO) {
        return authService.updateUserById(userConverter.operateUserDTO2userDO(userDTO))
                .map(userConverter::userDO2QueryUserDTO)
                .map(R::newInstance);
    }

    @Operation(summary = "查询用户分页列表")
    @RolesAllowed({"USER:ROOT"})
    @GET
    public Uni<R<PageDTO<QueryUserDTO>>> getUserInfo(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return authService.getUserPage(Pageable.of(page, size))
                .map(it -> PageDTO.converter(it, userConverter::userDO2QueryUserDTO))
                .map(R::newInstance);
    }

    @Operation(summary = "分配权限")
    @RolesAllowed({"USER:ROOT"})
    @PATCH
    @Path("/dispatchAuth")
    public Uni<R<Unit>> dispatchAuth(DispatchAuthDTO dispatchAuthDTO) {
        return authService.dispatchAuth(userConverter.dispatchAuthDTO2UserDO(dispatchAuthDTO))
                .map(R::newInstance);
    }

    @Operation(summary = "获取用户统计数据")
    @RolesAllowed({"USER:ROOT"})
    @GET
    @Path("/statistics")
    public Uni<R<UserStatisticsDTO>> getUserStatisticsData() {
        return authService.getUserStatistics()
                .map(userConverter::userStatisticsDO2userStatisticsDTO)
                .map(R::newInstance);
    }

}
