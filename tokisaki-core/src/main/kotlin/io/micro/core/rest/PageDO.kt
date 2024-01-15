package io.micro.core.rest

class PageDO<T> : Pageable() {

    var total: Long = 0

    var records: List<T> = listOf()

    companion object {

        @JvmStatic
        fun <E> of(pageable: Pageable, total: Long, records: List<E>) = PageDO<E>().also {
            it.current = pageable.current
            it.limit = pageable.limit
            it.records = records
            it.total = total
        }

        @JvmStatic
        fun <E> of(current: Int, limit: Int, total: Long, records: List<E>) = PageDO<E>().also {
            it.current = current
            it.limit = limit
            it.records = records
            it.total = total
        }

    }

}