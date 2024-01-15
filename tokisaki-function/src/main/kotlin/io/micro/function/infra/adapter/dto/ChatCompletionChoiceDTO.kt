package io.micro.function.infra.adapter.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ChatCompletionChoiceDTO(
    /**
     * This index of this completion in the returned list.
     */
    var index: Int? = null,

    /**
     * The [ChatMessageRoleDTO.ASSISTANT] message or delta (when streaming) which was generated
     */
    @JsonNames("delta")
    var message: ChatMessageDTO? = null,

    /**
     * The reason why GPT stopped generating, for example "length".
     */
    @SerialName("finish_reason")
    var finishReason: String? = null
)

