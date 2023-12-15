package io.micro.function.domain.image.model.valobj

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@Serializable
data class EmojiData(
    val alt: String,
    val emojiCodepoint: String,
    val gBoardOrder: Int,
    val combinations: List<EmojiCombination>
)
