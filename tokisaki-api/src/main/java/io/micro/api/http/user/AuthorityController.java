package io.micro.api.http.user;

import io.micro.api.user.converter.AuthorityConverter;
import io.micro.api.user.dto.OperateAuthorityDTO;
import io.micro.api.user.dto.QueryAuthorityDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.PageDTO;
import io.micro.core.rest.Pageable;
import io.micro.core.rest.R;
import io.micro.core.valid.ValidGroup;
import io.micro.server.auth.domain.service.AuthService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.ws.rs.*;
import kotlin.Unit;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "权限管理")
@InitAuthContext
@Path("/authority")
public class AuthorityController {

    @Inject
    public AuthService authService;

    @Inject
    public AuthorityConverter authorityConverter;

    @Operation(summary = "起停权限")
    @RolesAllowed({"USER:ROOT"})
    @PATCH
    @Path("/enabled")
    public Uni<R<Unit>> enabledAuthority(@Valid @ConvertGroup(to = ValidGroup.Update.class) OperateAuthorityDTO authority) {
        return authService.enabledAuthority(authorityConverter.operateAuthorityDTO2AuthorityDO((authority)))
                .map(R::newInstance);
    }

    @Operation(summary = "获取权限分页")
    @RolesAllowed({"USER:ROOT"})
    @GET
    public Uni<R<PageDTO<QueryAuthorityDTO>>> getAuthorityPage(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return authService.getAuthorityPage(Pageable.of(page, size))
                .map(pageDO -> PageDTO.converter(pageDO, authorityConverter::authorityDO2QueryAuthorityDTO))
                .map(R::newInstance);
    }

    @Operation(summary = "新增权限")
    @RolesAllowed({"USER:ROOT"})
    @POST
    public Uni<R<QueryAuthorityDTO>> addAuthority(@Valid @ConvertGroup(to = ValidGroup.Create.class) OperateAuthorityDTO authority) {
        return authService.addAuthority(authorityConverter.operateAuthorityDTO2AuthorityDO(authority))
                .map(authorityConverter::authorityDO2QueryAuthorityDTO)
                .map(R::newInstance);
    }

}
