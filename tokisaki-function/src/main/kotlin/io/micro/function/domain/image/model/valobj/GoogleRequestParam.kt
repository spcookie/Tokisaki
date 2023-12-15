package io.micro.function.domain.image.model.valobj

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@Serializable
data class GoogleRequestParam(
    val leftEmoji: String,
    val rightEmoji: String,
    val date: String
) {
    fun unicode(): GoogleRequestParam {
        return GoogleRequestParam("u${leftEmoji.lowercase()}", "u${rightEmoji.lowercase()}", date)
    }
}
