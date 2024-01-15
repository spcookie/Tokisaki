package io.micro.function.infra.adapter.gateway

import io.micro.core.exception.CmdException
import io.micro.function.infra.adapter.dto.ChatCompletionResultDTO
import io.quarkus.rest.client.reactive.ClientExceptionMapper
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 *@author Augenstern
 *@since 2023/10/21
 */
@RegisterRestClient(baseUri = "https://api.openai.com", configKey = "chat")
interface ChatGateway {
    @POST
    @Path("/v1/chat/completions")
    fun chatCompletions(
        request: String,
        @HeaderParam("Authorization") token: String
    ): Uni<ChatCompletionResultDTO>

    companion object {
        @JvmStatic
        @ClientExceptionMapper
        fun toException(response: Response): RuntimeException? {
            return if (response.status >= 400) {
                CmdException.fail(response.readEntity(String::class.java))
            } else {
                null
            }
        }
    }
}