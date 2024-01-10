package io.micro.core.function.dto

/**
 *@author Augenstern
 *@since 2023/10/8
 */
class MessageChain {
    var messages: MutableList<Message> = mutableListOf()
    var receipt: Receipt = Receipt()
    var meta: Map<String, String> = mapOf()

    companion object {
        fun text(msg: String): MessageChain {
            return MessageChain().apply {
                messages.add(Message(msg = msg))
            }
        }

        fun textChain(): MessageBuilder {
            return MessageBuilder()
        }

        fun image(bytes: ByteArray): MessageChain {
            return MessageChain().apply {
                messages.add(Message(data = Data(type = MediaType.Picture, bytes = bytes)))
            }
        }

        fun textAndImage(msg: String, bytes: ByteArray): MessageChain {
            return MessageChain().apply {
                messages.add(
                    Message(
                        msg = msg,
                        data = Data(type = MediaType.Picture, bytes = bytes)
                    )
                )
            }
        }
    }

    class MessageBuilder {
        private var messages: MutableList<Message> = mutableListOf()

        fun build(): MessageChain {
            return MessageChain().apply {
                messages = this@MessageBuilder.messages
            }
        }

        fun text(msg: String): MessageBuilder {
            messages.add(Message(msg))
            return this
        }
    }
}