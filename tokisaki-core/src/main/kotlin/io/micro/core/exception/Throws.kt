package io.micro.core.exception

import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
object Throws {

    fun fail(msg: String): Nothing {
        throw BusinessException(msg)
    }

    fun requireTure(msg: String, block: () -> Boolean) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        if (!block()) {
            fail(msg)
        }
    }

    fun requireTure(block: Boolean, msg: String) {
        if (!block) {
            fail(msg)
        }
    }

    fun requireFalse(msg: String, block: () -> Boolean) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        if (block()) {
            fail(msg)
        }
    }

    fun requireFalse(value: Boolean, msg: String) {
        if (value) {
            fail(msg)
        }
    }

    fun <T> requireNull(msg: String, block: () -> T) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        if (!Objects.isNull(block())) {
            fail(msg)
        }
    }

    fun <T> requireNull(value: T, msg: String) {
        if (!Objects.isNull(value)) {
            fail(msg)
        }
    }

    fun <T> requireNonNull(msg: String, block: () -> T) {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }
        if (Objects.isNull(block())) {
            fail(msg)
        }
    }

    fun <T> requireNonNull(value: T, msg: String) {
        contract {
            returns() implies (value != null)
        }
        if (Objects.isNull(value)) {
            fail(msg)
        }
    }

}