package io.micro.function.domain.image.adapter

import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.EmojiKitchen
import io.micro.function.domain.image.model.entity.Girl
import io.micro.function.domain.image.model.entity.Midjourney
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface ImageAdapter {
    fun fetchGirlImage(mode: String): Uni<Girl>

    fun fetchAnimeImage(): Uni<AnimeCartoon>

    fun fetchSyntheticEmoji(emojiKitchen: EmojiKitchen): Uni<Void>

    fun midjourneyGenerate(midjourney: Midjourney): Uni<Void>
}