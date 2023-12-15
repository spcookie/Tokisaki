package io.micro.function.infra.adapter.gateway

import io.micro.function.infra.adapter.dto.MidjourneyGenerateDTO
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 *@author Augenstern
 *@since 2023/10/10
 */
@RegisterRestClient(baseUri = "https://api.zhishuyun.com", configKey = "midjourney")
interface MidjourneyGateway {

    @POST
    @Path("/midjourney/imagine/relax")
    @Consumes(MediaType.APPLICATION_JSON)
    fun fetch(@QueryParam("token") mode: String, prompt: String): Uni<MidjourneyGenerateDTO>
}