package io.micro.function.domain.text.model.valobj

data class ChatCompletionChoice(
    /**
     * This index of this completion in the returned list.
     */
    var index: Int? = null,

    /**
     * The [ChatMessageRole.ASSISTANT] message or delta (when streaming) which was generated
     */
    var message: ChatMessage? = null,

    /**
     * The reason why GPT stopped generating, for example "length".
     */
    var finishReason: String? = null
)

