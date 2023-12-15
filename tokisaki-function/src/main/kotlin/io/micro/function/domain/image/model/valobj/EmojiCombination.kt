package io.micro.function.domain.image.model.valobj

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@Serializable
data class EmojiCombination(
    val alt: String,
    val leftEmojiCodepoint: String,
    val rightEmojiCodepoint: String,
    val date: String
)
