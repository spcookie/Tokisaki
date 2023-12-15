package io.micro.function.infra.adapter.gateway

import io.micro.function.infra.adapter.dto.RandomURLDTO
import io.quarkus.rest.client.reactive.ClientQueryParam
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.QueryParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 *@author Augenstern
 *@since 2023/10/10
 */
@RegisterRestClient(baseUri = "http://3650000.xyz/api/", configKey = "xyz")
interface XyzImageGateway {

    @GET
    @ClientQueryParam(name = "type", value = ["json"])
    fun fetchGirl(@QueryParam("mode") mode: String): Uni<RandomURLDTO>

    @GET
    @Path("360.php")
    @ClientQueryParam(name = "cid", value = ["26"])
    fun fetchAnime(): Uni<ByteArray>

}