package io.micro.core.rest

import kotlinx.serialization.Serializable

@Serializable
open class Pageable {

    var current: Int = 0

    var limit: Int = 0

    companion object {
        @JvmStatic
        fun of(current: Int, limit: Int) = Pageable().also {
            it.current = current
            it.limit = limit
        }
    }

}