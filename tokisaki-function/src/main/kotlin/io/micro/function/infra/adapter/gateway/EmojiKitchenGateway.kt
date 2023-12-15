package io.micro.function.infra.adapter.gateway

import io.smallrye.mutiny.Uni
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@RegisterRestClient(baseUri = "https://www.gstatic.com", configKey = "emoji")
interface EmojiKitchenGateway {
    @GET
    @Path("/android/keyboard/emojikitchen/{date}/{leftEmoji}/{leftEmoji}_{rightEmoji}.png")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    fun kitchen(
        @PathParam("date") date: String,
        @PathParam("leftEmoji") leftEmoji: String,
        @PathParam("rightEmoji") rightEmoji: String,
    ): Uni<ByteArray>
}