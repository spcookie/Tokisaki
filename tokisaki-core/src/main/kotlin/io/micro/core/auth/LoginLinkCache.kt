package io.micro.core.auth

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import jakarta.ws.rs.sse.SseEventSink
import java.time.Duration

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class LoginLinkCache private constructor() {

    val linkPool: Cache<String, SseEventSink> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(3))
        .removalListener<String, SseEventSink> { _, value, _ ->
            value?.close()
        }
        .build()

    val idPool: LoadingCache<String, RandomLoginCode.Code> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMillis(3))
        .build { RandomLoginCode.hire() }

    companion object {
        fun newInstance() = LoginLinkCache()
    }
}