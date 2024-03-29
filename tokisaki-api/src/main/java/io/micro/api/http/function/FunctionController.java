package io.micro.api.http.function;

import io.micro.api.function.converter.FunctionConverter;
import io.micro.api.function.dto.QueryFunctionDTO;
import io.micro.core.annotation.InitAuthContext;
import io.micro.core.rest.R;
import io.micro.core.util.ConverterKt;
import io.micro.server.robot.domain.service.FunctionService;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "功能操作")
@InitAuthContext
@Path("/function")
public class FunctionController {

    @Inject
    public FunctionService functionService;

    @Inject
    public FunctionConverter functionConverter;

    @Operation(summary = "查询用户可用功能")
    @GET
    @Path("/available")
    @Authenticated
    public Uni<R<List<QueryFunctionDTO>>> getUserFunctions() {
        return functionService.getUserFunctions()
                .map(list -> ConverterKt.converter(list, functionConverter::functionDO2functionDTO))
                .map(R::newInstance);
    }

}