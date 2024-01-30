package io.micro.function.domain.image.service.impl

import io.micro.core.annotation.CallCount
import io.micro.core.exception.CmdException
import io.micro.core.function.dto.MessageChain
import io.micro.core.function.sdk.CommandService
import io.micro.function.domain.image.adapter.ImageAdapter
import io.micro.function.domain.image.model.entity.Girl
import io.micro.function.domain.image.repository.ImageRepository
import io.micro.function.domain.image.service.ImageTask
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
 *@since 2023/10/10
 */
@ApplicationScoped
class GirlServiceImpl(
    private val imageRepository: ImageRepository,
    private val imageAdapter: ImageAdapter
) : CommandService, ImageTask {

    override fun cmd() = Girl.identify()

    override fun describe(): Uni<String> {
        return imageRepository.findCallStatistic(cmd().cmd)
            .flatMap { Girl.describe(it) }
    }

    @CallCount
    @WithTransaction
    @Fallback(fallbackMethod = "fallback", applyOn = [RateLimitException::class])
    @RateLimit(value = 20, windowUnit = ChronoUnit.MINUTES, minSpacing = 1)
    override fun invoke(args: MutableList<String>, config: Map<String, *>): Uni<MessageChain> {
        return imageRepository.findGirlImage()
            .onItem().ifNotNull().call { girl ->
                imageRepository.modifyImageUsed(girl.id!!)
            }
            .onItem().ifNull().switchTo {
                imageAdapter.fetchGirlImage(Girl.obtainMode())
            }
            .onFailure().transform { CmdException.fail(it.message.toString()) }
            .flatMap(::generateMessageChain)
    }

    fun fallback(
        args: MutableList<String>,
        config: Map<String, Any>,
        rateLimitException: RateLimitException
    ): Uni<MessageChain> {
        return Uni.createFrom().failure(CmdException.failForUni("命令“g”已达到速率限制"))
    }

    private fun generateMessageChain(girl: Girl): Uni<MessageChain> {
        return Uni.createFrom().item(MessageChain.image(girl.image))
    }

    @WithTransaction
    override fun download(): Uni<Void> {
        Log.debug("Girl定时任务开始...")
        return imageRepository.countGirlImageUnused().flatMap { num ->
            if (num < 30) {
                imageAdapter.fetchGirlImage(Girl.obtainMode()).flatMap { girl ->
                    girl.generatePath()
                    imageRepository.saveImage(girl)
                }
            } else {
                Log.warn("Girl图片下载已达最大值: $num")
                Uni.createFrom().voidItem()
            }
        }
    }

    @WithTransaction
    override fun remove(): Uni<Void> {
        return imageRepository.findGirlImageUsed().flatMap(imageRepository::removeImages)
    }
}