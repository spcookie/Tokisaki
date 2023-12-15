package io.micro.function.domain.text.model.entity

import io.micro.core.fundto.Cmd
import io.micro.core.funsdk.AbstractCmd
import io.micro.core.funsdk.ArgsMergeStrategy
import io.micro.function.domain.text.model.valobj.*
import io.net.spcokie.domain.text.model.Text
import io.net.spcokie.types.QQFace
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/20
 */
class Chat : Text() {

    private lateinit var chatConfig: ChatConfig

    lateinit var prompt: String

    lateinit var messages: MutableList<Pair<ChatMessage, Long>>

    companion object : AbstractCmd() {
        fun create(
            chatConfig: ChatConfig,
            args: MutableList<String>,
            messages: MutableList<Pair<ChatMessage, Long>>
        ): Chat {
            return Chat().apply {
                this.chatConfig = chatConfig
                this.prompt = args().strategy(args)[0]
                this.messages = messages
            }
        }

        override fun identify() = Cmd.Chat

        override fun args(): ArgsMergeStrategy {
            return ArgsMergeStrategy.Merge
        }

        override fun describe(call: Long): Uni<String> {
            return Uni.createFrom().item {
                """
                    ${QQFace.YING_HUA}聊天 ◟$call◞
                        ╰c(text)
                          └text-聊天内容
                    """.trimIndent()
            }
        }
    }

    private fun ensureEnoughSpace() {
        if (messages.isNotEmpty()) {
            val totalTokens = messages.sumOf { it.second }
            val nextTokens = totalTokens.div(messages.size.div(2))
            while (chatConfig.maxTokens() - totalTokens < nextTokens && messages.isNotEmpty()) {
                messages.removeFirst()
            }
        }
    }

    fun prepareRequest(): ChatCompletionRequest {
        ensureEnoughSpace()
        val msg = messages.map { it.first }.toMutableList()
        msg.add(ChatMessage(ChatMessageRole.USER.value(), prompt))
        return ChatCompletionRequest().apply {
            key = chatConfig.apiKey()
            model = chatConfig.model()
            messages = msg
        }
    }

    fun attachResultMessage(message: String, usage: Usage) {
        messages.add(ChatMessage(ChatMessageRole.USER.value(), prompt) to usage.promptTokens)
        messages.add(ChatMessage(ChatMessageRole.ASSISTANT.value(), message) to usage.completionTokens)
        content = message
    }
}