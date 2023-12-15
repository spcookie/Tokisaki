package io.micro.function.domain.image.service.impl

import io.micro.core.annotation.CallCount
import io.micro.core.fundto.MessageChain
import io.micro.core.funsdk.CommandService
import io.micro.function.domain.image.adapter.ImageAdapter
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.repository.ImageRepository
import io.micro.function.domain.image.service.ImageTask
import io.net.spcokie.common.exception.CmdException
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.logging.Log
import io.smallrye.faulttolerance.api.RateLimit
import io.smallrye.faulttolerance.api.RateLimitException
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.faulttolerance.Fallback
import java.time.temporal.ChronoUnit

/**
 *@author Augenstern
 *@since 2023/10/22
 */
@ApplicationScoped
class AnimeCartoonServiceImpl(
    private val imageRepository: ImageRepository,
    private val imageAdapter: ImageAdapter
) : CommandService, ImageTask {
    override fun cmd() = AnimeCartoon.identify()

    override fun describe(): Uni<String> {
        return imageRepository.findCallStatistic(cmd().code).flatMap {
            AnimeCartoon.describe(it)
        }
    }

    @CallCount
    @WithTransaction
    @Fallback(fallbackMethod = "fallback", applyOn = [RateLimitException::class])
    @RateLimit(value = 20, windowUnit = ChronoUnit.MINUTES, minSpacing = 1)
    override fun invoke(args: MutableList<String>): Uni<MessageChain> {
        return imageRepository.findAnimeImage()
            .onItem().ifNotNull().call { ac ->
                imageRepository.modifyImageUsed(ac.id!!)
            }
            .onItem().ifNull().switchTo {
                imageAdapter.fetchAnimeImage()
            }
            .map { ac ->
                MessageChain.image(ac.image)
            }
    }

    fun fallback(args: MutableList<String>, rateLimitException: RateLimitException): Uni<MessageChain> {
        return Uni.createFrom().failure(CmdException.failForUni("命令“ac”已达到速率限制"))
    }

    @WithTransaction
    override fun download(): Uni<Void> {
        Log.debug("Anime定时任务开始...")
        return imageRepository.countAnimeImageUnused().flatMap { num ->
            if (num < 10) {
                imageAdapter.fetchAnimeImage().flatMap { anime ->
                    anime.generatePath()
                    imageRepository.saveImage(anime)
                }
            } else {
                Log.warn("Anime图片下载已达最大值: $num")
                Uni.createFrom().voidItem()
            }
        }
    }

    override fun remove(): Uni<Void> {
        return imageRepository.findAnimeImageUsed().flatMap(imageRepository::removeImages)
    }
}