package io.micro.function.infra.adapter.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageDTO(
    /**
     * The number of prompt tokens used.
     */
    @SerialName("prompt_tokens")
    var promptTokens: Long = 0,

    /**
     * The number of completion tokens used.
     */
    @SerialName("completion_tokens")
    var completionTokens: Long = 0,

    /**
     * The number of total tokens used
     */
    @SerialName("total_tokens")
    var totalTokens: Long = 0
)