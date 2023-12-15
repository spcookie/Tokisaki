package io.micro.function.domain.image.repository

import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.Girl
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/8
 */
interface ImageRepository {

    fun findCallStatistic(key: String): Uni<Long>

    fun findGirlImage(): Uni<Girl>

    fun findAnimeImage(): Uni<AnimeCartoon>

    fun modifyImageUsed(id: Long): Uni<Void>

    fun countGirlImageUnused(): Uni<Long>

    fun countAnimeImageUnused(): Uni<Long>

    fun saveImage(image: Image): Uni<Void>

    fun findGirlImageUsed(): Uni<List<Girl>>

    fun findAnimeImageUsed(): Uni<List<AnimeCartoon>>

    fun removeImages(images: List<Image>): Uni<Void>
}