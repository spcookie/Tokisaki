package io.micro.function.infra.repository

import io.micro.function.domain.text.model.valobj.ChatMessage
import io.micro.function.domain.text.repository.TextRepository
import io.micro.function.infra.cache.TextCache
import io.micro.function.infra.converter.TextConverter
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped

/**
 *@author Augenstern
 *@since 2023/10/20
 */
@ApplicationScoped
class TextRepositoryImpl(
    private val textCache: TextCache,
    private val textConverter: TextConverter
) : TextRepository {
    override fun findCallStatistic(key: String): Uni<Long> {
        return Uni.createFrom().item(0)
    }

    override fun requireTenantLock(key: String): Uni<Boolean> {
        return textCache.getChatLock(key)
    }

    override fun releaseTenantLock(key: String): Uni<Boolean> {
        return textCache.removeChatLock(key)
    }

    override fun findTenantMessage(key: String): Uni<MutableList<Pair<ChatMessage, Long>>> {
        return textCache.getChatMessages(key).onItem().ifNull().continueWith(mutableListOf())
            .map(textConverter::toChatPairMessageDOList)
    }

    override fun saveTenantMessage(key: String, value: MutableList<Pair<ChatMessage, Long>>): Uni<Void> {
        val list = textConverter.toChatPairMessageDTOList(value)
        return textCache.saveChatMessages(key, list)
    }
}