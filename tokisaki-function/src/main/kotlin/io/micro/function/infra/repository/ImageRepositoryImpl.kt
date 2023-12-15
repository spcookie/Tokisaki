package io.net.spcokie.infra.repository

import io.micro.core.aop.CommandCache
import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.Girl
import io.micro.function.domain.image.repository.ImageRepository
import io.net.spcokie.infra.converter.ImageConverter
import io.net.spcokie.infra.oss.ImageOss
import io.net.spcokie.infra.po.ImagePO
import io.net.spcokie.types.ImageCategory
import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.smallrye.mutiny.Uni
import io.vertx.mutiny.core.Vertx
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/10/8
 */
@ApplicationScoped
class ImageRepositoryImpl(
    private val commandCache: CommandCache,
    private val imageOss: ImageOss,
    private val imageConverter: ImageConverter
) : ImageRepository {

    override fun findCallStatistic(key: String): Uni<Long> {
        return commandCache.fetchCallStatistic(key).onItem().ifNull().continueWith(0)
    }

    @WithSession
    override fun findGirlImage(): Uni<Girl> {
        return ImagePO.selectIsNotUsedByCategory(ImageCategory.GIRL)
            .onItem().ifNotNull().transform(imageConverter::toGirlDO)
            .flatMap(fetchImage())
    }

    @WithSession
    override fun findAnimeImage(): Uni<AnimeCartoon> {
        return ImagePO.selectIsNotUsedByCategory(ImageCategory.ANIME)
            .onItem().ifNotNull().transform(imageConverter::toAnimeDO)
            .flatMap(fetchImage())
    }

    private fun <T : Image?> fetchImage(): (T) -> Uni<T> {
        return { ac: T ->
            if (ac != null) {
                val context = Vertx.currentContext()
                imageOss.fetchImage(ac.path).map { bytes ->
                    ac.also {
                        it.image = bytes
                    }
                }.emitOn { runnable -> context.runOnContext(runnable) }
            } else {
                Uni.createFrom().nullItem()
            }
        }
    }

    @WithSession
    override fun modifyImageUsed(id: Long): Uni<Void> {
        return ImagePO.updateUsedById(id).replaceWithVoid()
    }

    @WithSession
    override fun countGirlImageUnused(): Uni<Long> {
        return ImagePO.countUnusedByCategory(ImageCategory.GIRL)
    }

    @WithSession
    override fun countAnimeImageUnused(): Uni<Long> {
        return ImagePO.countUnusedByCategory(ImageCategory.ANIME)
    }

    @WithSession
    override fun saveImage(image: Image): Uni<Void> {
        return Uni.createFrom().item(image)
            .map(imageConverter::toImagePO)
            .call { po ->
                po.used = false
                po.persistAndFlushWithUnused()
                    .call { _ ->
                        val context = Vertx.currentContext()
                        imageOss.saveImage(image.path, image.image)
                            .emitOn { runnable -> context.runOnContext(runnable) }
                    }
            }.replaceWithVoid()
    }

    @WithSession
    override fun findGirlImageUsed(): Uni<List<Girl>> {
        return ImagePO.selectImageUsedByCategory(ImageCategory.GIRL).map { list ->
            list.map(imageConverter::toGirlDO)
        }
    }

    @WithSession
    override fun findAnimeImageUsed(): Uni<List<AnimeCartoon>> {
        return ImagePO.selectImageUsedByCategory(ImageCategory.ANIME).map { list ->
            list.map(imageConverter::toAnimeDO)
        }
    }

    @WithSession
    override fun removeImages(images: List<Image>): Uni<Void> {
        return if (images.isEmpty()) {
            Uni.createFrom().voidItem()
        } else {
            Uni.createFrom().deferred {
                ImagePO.deleteImageByIds(images.mapNotNull(Image::id))
            }.flatMap {
                imageOss.removeImages(images.map(Image::path))
            }
        }
    }

}