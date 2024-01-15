package io.micro.function.infra.adapter

import io.micro.core.exception.CmdException
import io.micro.core.exception.RetryableException
import io.micro.function.domain.image.adapter.ImageAdapter
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.EmojiKitchen
import io.micro.function.domain.image.model.entity.Girl
import io.micro.function.domain.image.model.entity.Midjourney
import io.micro.function.infra.adapter.dto.MidjourneyPromptDTO
import io.micro.function.infra.adapter.gateway.EmojiKitchenGateway
import io.micro.function.infra.adapter.gateway.MidjourneyGateway
import io.micro.function.infra.adapter.gateway.XyzImageGateway
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.client.WebClient
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.microprofile.faulttolerance.Fallback
import org.eclipse.microprofile.faulttolerance.Retry
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.net.URI
import java.time.temporal.ChronoUnit

/**
 *@author Augenstern
 *@since 2023/10/10
 */
@ApplicationScoped
class ImageAdapterImpl(
    @RestClient private val xyzImageGateway: XyzImageGateway,
    @RestClient private val midjourneyGateway: MidjourneyGateway,
    @RestClient private val emojiKitchenGateway: EmojiKitchenGateway,
    private val webClient: WebClient
) : ImageAdapter {

    @Fallback(fallbackMethod = "fetchGirlImageFallback")
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS)
    override fun fetchGirlImage(mode: String): Uni<Girl> {
        return xyzImageGateway.fetchGirl(mode).flatMap { (code, url) ->
            if (code == 200) {
                Uni.createFrom().emitter { em ->
                    URI.create(url).apply {
                        webClient.get(80, host, path)
                            .send()
                            .onSuccess {
                                Log.debug("Girl图片下载成功...")
                                em.complete(it.body().bytes)
                            }.onFailure {
                                Log.error(it)
                                em.fail(it)
                            }
                    }
                }
            } else {
                Uni.createFrom().failure(RetryableException())
            }
        }.map { Girl().apply { image = it } }
    }

    fun fetchGirlImageFallback(mode: String): Uni<Girl> {
        return fetchImageFallback()
    }

    @Fallback(fallbackMethod = "fetchAnimeImageFallback")
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS)
    override fun fetchAnimeImage(): Uni<AnimeCartoon> {
        return xyzImageGateway.fetchAnime().map {
            AnimeCartoon().apply {
                image = it
            }
        }
    }

    fun fetchAnimeImageFallback(): Uni<AnimeCartoon> {
        return fetchImageFallback()
    }

    @Fallback(fallbackMethod = "fetchSyntheticEmojiFallback")
    @Retry(maxRetries = 1, delay = 1, delayUnit = ChronoUnit.SECONDS)
    override fun fetchSyntheticEmoji(emojiKitchen: EmojiKitchen): Uni<Void> {
        val (leftEmoji, rightEmoji, date) = emojiKitchen.googleRequestParam
        return emojiKitchenGateway.kitchen(date, leftEmoji, rightEmoji).invoke { bytes ->
            emojiKitchen.apply {
                image = bytes
            }
        }.replaceWithVoid()
    }

    fun fetchSyntheticEmojiFallback(emojiKitchen: EmojiKitchen): Uni<Void> {
        return fetchImageFallback()
    }

    @Fallback(fallbackMethod = "fetchMidjourneyImageFallback")
    override fun midjourneyGenerate(midjourney: Midjourney): Uni<Void> {
        return midjourneyGateway.fetch(
            midjourney.config.apiKey,
            Json.encodeToString(MidjourneyPromptDTO(midjourney.prompt))
        ).flatMap { dto ->
            Log.debug(dto)
            Uni.createFrom().emitter { em ->
                URI.create(dto.imageUrl).run {
                    webClient.get(80, host, path)
                        .send()
                        .onSuccess {
                            Log.debug("Midjourney图片下载成功...")
                            em.complete(dto.imageUrl to it.body().bytes)
                        }
                        .onFailure {
                            Log.error(it)
                            em.fail(it)
                        }
                }
            }
        }.invoke { pair ->
            val (path, bytes) = pair
            midjourney.apply {
                this.path = path
                this.image = bytes
            }
        }.replaceWithVoid()
    }

    fun fetchMidjourneyImageFallback(midjourney: Midjourney): Uni<Void> {
        return fetchImageFallback()
    }

    private fun <T> fetchImageFallback(): Uni<T> {
        return Uni.createFrom().failure(CmdException.failForUni("获取图片失败"))
    }
}