package io.micro.function.domain.image.model.valobj

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@Serializable
data class EmojiMetadata(
    val knownSupportedEmoji: List<String>,
    val data: Map<String, EmojiData>
)
