package io.micro.function.infra.adapter.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatCompletionResultDTO(
    /**
     * Unique id assigned to this chat completion.
     */
    var id: String? = null,

    /**
     * The type of object returned, should be "chat.completion"
     */
    var `object`: String? = null,

    /**
     * The creation time in epoch seconds.
     */
    var created: Long = 0,

    /**
     * The GPT model used.
     */
    var model: String? = null,

    /**
     * A list of all generated completions.
     */
    var choices: List<ChatCompletionChoiceDTO>? = null,

    /**
     * The API usage for this request.
     */
    var usage: UsageDTO? = null
)