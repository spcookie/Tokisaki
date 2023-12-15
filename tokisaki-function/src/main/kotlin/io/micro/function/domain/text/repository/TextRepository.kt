package io.micro.function.domain.text.repository

import io.micro.function.domain.text.model.valobj.ChatMessage
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/20
 */
interface TextRepository {
    fun findCallStatistic(key: String): Uni<Long>

    fun requireTenantLock(key: String): Uni<Boolean>

    fun releaseTenantLock(key: String): Uni<Boolean>

    fun findTenantMessage(key: String): Uni<MutableList<Pair<ChatMessage, Long>>>

    fun saveTenantMessage(key: String, value: MutableList<Pair<ChatMessage, Long>>): Uni<Void>
}