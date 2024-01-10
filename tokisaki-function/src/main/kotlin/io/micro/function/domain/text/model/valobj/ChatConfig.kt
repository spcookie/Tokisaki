package io.micro.function.domain.text.model.valobj

/**
 *@author Augenstern
 *@since 2023/10/20
 */
class ChatConfig(val map: Map<String, *>) {

    val model: String by map

    val maxTokens: Int by map

    val apiKey: String by map

}