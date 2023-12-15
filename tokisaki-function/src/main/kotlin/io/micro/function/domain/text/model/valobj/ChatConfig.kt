package io.micro.function.domain.text.model.valobj

import io.smallrye.config.ConfigMapping

/**
 *@author Augenstern
 *@since 2023/10/20
 */
@ConfigMapping(prefix = "chat")
interface ChatConfig {
    fun model(): String

    fun maxTokens(): Int

    fun apiKey(): String
}