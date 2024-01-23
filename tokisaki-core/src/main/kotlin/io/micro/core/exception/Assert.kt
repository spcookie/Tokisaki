package io.micro.core.exception

import io.micro.core.rest.CommonCode
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@JvmOverloads
fun fail(detail: String? = null, code: CommonCode = CommonCode.FAIL): Nothing {
    throw BusinessException(code, detail)
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun requireTure(detail: String = "", code: CommonCode = CommonCode.FAIL, block: () -> Boolean): Boolean {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (!block()) {
        fail(detail, code)
    } else {
        return true
    }
}

@JvmOverloads
fun requireTure(value: Boolean, detail: String? = null, code: CommonCode = CommonCode.FAIL): Boolean {
    if (!value) {
        fail(detail, code)
    } else {
        return true
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun requireFalse(detail: String? = null, code: CommonCode = CommonCode.FAIL, block: () -> Boolean): Boolean {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (block()) {
        fail(detail, code)
    } else {
        return false
    }
}

@JvmOverloads
fun requireFalse(value: Boolean, detail: String? = null, code: CommonCode = CommonCode.FAIL): Boolean {
    if (value) {
        fail(detail, code)
    } else {
        return false
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNonNull(detail: String? = null, code: CommonCode = CommonCode.NOT_FOUND, block: () -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    val result = block()
    if (Objects.isNull(result)) {
        fail(detail, code)
    } else {
        return result
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNonNull(value: T, detail: String? = null, code: CommonCode = CommonCode.NOT_FOUND): T {
    contract {
        returns() implies (value != null)
    }
    if (Objects.isNull(value)) {
        fail(detail, code)
    } else {
        return value
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNull(value: T, detail: String? = null, code: CommonCode = CommonCode.FAIL): T? {
    contract {
        returns() implies (value == null)
    }
    if (!Objects.isNull(value)) {
        fail(detail, code)
    } else {
        return null
    }
}

@JvmOverloads
fun failWith(detail: String? = null, code: CommonCode = CommonCode.FAIL): BusinessException {
    return BusinessException(code, detail)
}