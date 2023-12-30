package io.micro.api.function

import io.micro.api.function.converter.FunctionConverter
import io.micro.api.function.dto.FunctionDTO
import io.micro.core.annotation.InitAuthContext
import io.micro.server.function.domain.service.FunctionService
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import org.eclipse.microprofile.openapi.annotations.Operation

@InitAuthContext
@Path("/function")
class FunctionController {

    @Inject
    lateinit var functionService: FunctionService

    @Inject
    lateinit var functionConverter: FunctionConverter

    @Operation(summary = "查询用户可用功能")
    @GET
    @Path("/available")
    fun getUserFunctions(): Uni<List<FunctionDTO>> {
        return functionService.getUserFunctions()
            .map { functionDOs ->
                functionDOs.map { functionConverter.functionDO2functionDTO(it) }
            }
    }

}