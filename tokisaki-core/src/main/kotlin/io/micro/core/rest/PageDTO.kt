package io.micro.core.rest

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class PageDTO<T> : Pageable() {

    var total: Int by Delegates.notNull()

    var records: List<T> = listOf()

    companion object {
        fun <E> of(current: Int, limit: Int, records: List<E>) = PageDTO<E>().also {
            it.current = current
            it.limit = limit
            it.records = records
            it.total = records.size
        }
    }

}