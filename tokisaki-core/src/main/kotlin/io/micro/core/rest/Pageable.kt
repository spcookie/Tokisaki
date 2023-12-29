package io.micro.core.rest

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
open class Pageable {

    open var current: Int by Delegates.notNull()

    open var limit: Int by Delegates.notNull()

    companion object {
        fun of(current: Int, limit: Int) = Pageable().also {
            it.current = current
            it.limit = limit
        }
    }

}