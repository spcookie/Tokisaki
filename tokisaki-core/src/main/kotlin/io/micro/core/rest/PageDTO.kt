package io.micro.core.rest

import kotlinx.serialization.Serializable

@Serializable
class PageDTO<T> : Pageable() {

    var total: Long = 0

    var records: List<T> = listOf()

    companion object {
        @JvmStatic
        fun <E> of(current: Int, limit: Int, total: Long, records: List<E>) = PageDTO<E>().also {
            it.current = current
            it.limit = limit
            it.records = records
            it.total = total
        }
    }

}