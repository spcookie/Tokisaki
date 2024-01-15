package io.micro.function.infra.cache

import io.micro.function.infra.adapter.dto.ChatMessageDTO
import io.quarkus.redis.datasource.ReactiveRedisDataSource
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 *@author Augenstern
 *@since 2023/10/20
 */
@ApplicationScoped
class TextCache(private val ds: ReactiveRedisDataSource, private val json: Json) {

    companion object {
        private const val CHAT_LOCK = "chatLock"
        private const val CHAT_MESSAGE = "chatMessage"
    }

    fun getChatLock(key: String): Uni<Boolean> {
        val lockKey = "$CHAT_LOCK:$key"
        return ds.value(String::class.java).setnx(lockKey, CHAT_LOCK).call { locked ->
            if (locked) {
                ds.key().expire(lockKey, 60 * 3)
            } else {
                Uni.createFrom().voidItem()
            }
        }
    }

    fun removeChatLock(key: String): Uni<Boolean> {
        return ds.key().del("$CHAT_LOCK:$key").map { it > 0 }
    }

    fun getChatMessages(key: String): Uni<MutableList<Pair<ChatMessageDTO, Long>>> {
        return ds.value(String::class.java)["$CHAT_MESSAGE:$key"].onItem().ifNotNull()
            .transform {
                json.decodeFromString<MutableList<Pair<ChatMessageDTO, Long>>>(it)
            }
    }

    fun saveChatMessages(key: String, value: MutableList<Pair<ChatMessageDTO, Long>>): Uni<Void> {
        return ds.value(String::class.java).set("$CHAT_MESSAGE:$key", json.encodeToString(value))
    }
}