package io.micro.function.domain.text.model.valobj


/**
 * see [ChatMessage] documentation.
 */
enum class ChatMessageRole(private val value: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    FUNCTION("function");

    fun value(): String {
        return value
    }

    override fun toString(): String {
        return "ChatMessageRole(value='$value')"
    }
}
