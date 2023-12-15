package io.micro.function.infra.adapter

import io.micro.function.domain.text.adapter.TextAdapter
import io.micro.function.domain.text.model.valobj.ChatCompletionRequest
import io.micro.function.domain.text.model.valobj.ChatCompletionResult
import io.micro.function.infra.adapter.gateway.ChatGateway
import io.net.spcokie.infra.converter.TextConverter
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.microprofile.rest.client.inject.RestClient

/**
 *@author Augenstern
 *@since 2023/10/20
 */
@ApplicationScoped
class TextAdapterImpl(
    @RestClient private val chatGateway: ChatGateway,
    private val textConverter: TextConverter
) : TextAdapter {

    override fun processRequest(request: ChatCompletionRequest): Uni<ChatCompletionResult> {
        val requestDTO = textConverter.toChatCompletionRequestDTO(request)
        return chatGateway.chatCompletions(Json.encodeToString(requestDTO), "Bearer ${request.key}")
            .onFailure().invoke { ex ->
                Log.error("处理聊天请求时出错", ex)
            }
            .map(textConverter::toChatCompletionResultDO)
    }
}