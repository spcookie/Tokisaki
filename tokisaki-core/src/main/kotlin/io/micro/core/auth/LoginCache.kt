package io.micro.core.auth

import cn.hutool.core.util.IdUtil
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import jakarta.ws.rs.sse.SseEventSink
import java.time.Duration

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class LoginCache private constructor() {

    val linkPool: Cache<String, SseEventSink> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(3))
        .removalListener<String, SseEventSink> { _, value, _ ->
            value?.close()
        }
        .build()

    val idPool: LoadingCache<String, String> = Caffeine.newBuilder()
        .expireAfterAccess(Duration.ofMillis(3))
        .build {
            // TODO
            IdUtil.nanoId()
            IdUtil.getDataCenterId(9999L).toString()
        }

    companion object {
        fun newInstance() = LoginCache()
    }
}