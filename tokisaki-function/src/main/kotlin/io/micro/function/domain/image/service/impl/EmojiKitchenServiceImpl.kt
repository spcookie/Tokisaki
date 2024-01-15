package io.micro.function.domain.image.service.impl

import io.micro.core.annotation.CallCount
import io.micro.core.exception.CmdException
import io.micro.core.function.dto.MessageChain
import io.micro.core.function.sdk.CommandService
import io.micro.function.domain.image.adapter.ImageAdapter
import io.micro.function.domain.image.model.entity.EmojiKitchen
import io.micro.function.domain.image.repository.ImageRepository
import io.quarkus.logging.Log
import io.quarkus.runtime.StartupEvent
import io.smallrye.faulttolerance.api.RateLimit
import io.smallrye.faulttolerance.api.RateLimitException
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import kotlinx.serialization.json.Json
import org.eclipse.microprofile.faulttolerance.Fallback
import java.time.temporal.ChronoUnit

/**
 *@author Augenstern
 *@since 2023/10/23
 */
@ApplicationScoped
class EmojiKitchenServiceImpl(
    private val imageRepository: ImageRepository,
    private val imageAdapter: ImageAdapter
) : CommandService {

    private val json = Json { ignoreUnknownKeys = true }

    override fun cmd() = EmojiKitchen.identify()

    override fun describe(): Uni<String> {
        return imageRepository.findCallStatistic(cmd().code).flatMap {
            EmojiKitchen.describe(it)
        }
    }

    @CallCount
    @Fallback(fallbackMethod = "fallback", applyOn = [RateLimitException::class])
    @RateLimit(value = 20, windowUnit = ChronoUnit.MINUTES, minSpacing = 1)
    override fun invoke(args: MutableList<String>, config: Map<String, *>): Uni<MessageChain> {
        val emojiKitchen = EmojiKitchen.build(args)
        return imageAdapter.fetchSyntheticEmoji(emojiKitchen)
            .map { MessageChain.image(emojiKitchen.image) }
    }

    fun fallback(
        args: MutableList<String>,
        config: Map<String, Any>,
        rateLimitException: RateLimitException
    ): Uni<MessageChain> {
        return Uni.createFrom().failure(CmdException.failForUni("命令“e”已达到速率限制"))
    }

    fun initEmojiMetadata(@Observes startupEvent: StartupEvent) {
        try {
            Log.debug("开始读取${EmojiKitchen.EMOJI_METADATA}数据...")
            val resourceStream = this::class.java.classLoader.getResourceAsStream(EmojiKitchen.EMOJI_METADATA)
            if (resourceStream != null) {
                resourceStream.use {
                    EmojiKitchen.emojiMetadata = json.decodeFromString(String(it.readBytes()))
                }
            } else {
                Log.error("读取${EmojiKitchen.EMOJI_METADATA}失败")
            }
        } catch (ex: RuntimeException) {
            Log.error("加载${EmojiKitchen.EMOJI_METADATA}失败", ex)
        }
    }

}