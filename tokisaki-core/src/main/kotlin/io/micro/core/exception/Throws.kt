package io.micro.core.exception

import java.util.*

object Throws {

    fun fail(msg: String): Nothing {
        throw BusinessException(msg)
    }

    fun failIfTure(block: () -> Boolean, msg: String) {
        if (block()) {
            fail(msg)
        }
    }

    fun failIfFalse(block: () -> Boolean, msg: String) {
        if (!block()) {
            fail(msg)
        }
    }

    fun <T> failIfNull(block: () -> T, msg: String) {
        if (Objects.isNull(block())) {
            fail(msg)
        }
    }

    fun <T> failIfNonNull(block: () -> T, msg: String) {
        if (!Objects.isNull(block())) {
            fail(msg)
        }
    }
}