package io.micro.function.domain.image.service.impl

import io.micro.core.annotation.CallCount
import io.micro.core.function.dto.MessageChain
import io.micro.core.function.sdk.CommandService
import io.micro.function.domain.image.adapter.ImageAdapter
import io.micro.function.domain.image.model.entity.Midjourney
import io.micro.function.domain.image.model.valobj.MidjourneyConfig
import io.micro.function.domain.image.repository.ImageRepository
import io.net.spcokie.common.exception.CmdException
import io.smallrye.faulttolerance.api.RateLimit
import io.smallrye.faulttolerance.api.RateLimitException
import io.smallrye.faulttolerance.api.RateLimitType
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.faulttolerance.Fallback
import java.time.temporal.ChronoUnit

/**
 *@author Augenstern
 *@since 2023/10/19
 */
@ApplicationScoped
class MidjourneyServiceImpl(
    private val imageAdapter: ImageAdapter,
    private val imageRepository: ImageRepository,
) : CommandService {
    override fun cmd() = Midjourney.identify()

    override fun describe(): Uni<String> {
        return imageRepository.findCallStatistic(cmd().code)
            .flatMap {
                Midjourney.describe(it)
            }
    }

    @CallCount
    @Fallback(fallbackMethod = "fallback", applyOn = [RateLimitException::class])
    @RateLimit(
        value = 20,
        windowUnit = ChronoUnit.HOURS,
        minSpacing = 3,
        minSpacingUnit = ChronoUnit.MINUTES,
        type = RateLimitType.ROLLING
    )
    override fun invoke(args: MutableList<String>, config: Map<String, *>): Uni<MessageChain> {
        val midjourney = Midjourney.create(MidjourneyConfig(), Midjourney.args().strategy(args)[0])
        return imageAdapter.midjourneyGenerate(midjourney).map {
            MessageChain.image(midjourney.image)
        }
    }

    fun fallback(
        args: MutableList<String>,
        config: Map<String, Any>,
        rateLimitException: RateLimitException
    ): Uni<MessageChain> {
        return Uni.createFrom().failure(CmdException.failForUni("命令“mj”已达到速率限制"))
    }
}