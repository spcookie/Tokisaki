package io.micro.function.infra.adapter.dto


/**
 * see [ChatMessageDTO] documentation.
 */
enum class ChatMessageRoleDTO(private val value: String) {
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
