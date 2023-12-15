package io.micro.function.domain.text.adapter

import io.micro.function.domain.text.model.valobj.ChatCompletionRequest
import io.micro.function.domain.text.model.valobj.ChatCompletionResult
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/20
 */
interface TextAdapter {
    fun processRequest(request: ChatCompletionRequest): Uni<ChatCompletionResult>
}