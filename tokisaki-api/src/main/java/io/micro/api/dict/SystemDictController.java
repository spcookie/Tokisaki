package io.micro.api.dict;

import io.micro.api.dict.converter.SystemDictConverter;
import io.micro.api.dict.dto.QuerySystemDictDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.PageDTO;
import io.micro.core.rest.Pageable;
import io.micro.core.rest.R;
import io.micro.server.dict.domain.service.SystemDictService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "系统字典")
@InitAuthContext
@Path("/system/dict")
public class SystemDictController {

    @Inject
    public SystemDictService systemDictService;

    @Inject
    public SystemDictConverter systemDictConverter;

    @Operation(summary = "查询系统数据字典")
    @GET
    public Uni<R<PageDTO<QuerySystemDictDTO>>> getSystemDictPage(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("size") @DefaultValue("10") int size) {
        return systemDictService.findDictPage(Pageable.of(page, size))
                .map(paged -> {
                    List<QuerySystemDictDTO> list = paged.getFirst().stream()
                            .map(it -> systemDictConverter.systemDictDO2QuerySystemDictDTO(it))
                            .toList();
                    return R.newInstance(PageDTO.of(page, size, paged.getSecond(), list));
                });
    }

}
