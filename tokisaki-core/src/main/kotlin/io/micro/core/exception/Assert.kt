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
fun requireTure(detail: String = "", code: CommonCode = CommonCode.FAIL, block: () -> Boolean) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (!block()) {
        fail(detail, code)
    }
}

@JvmOverloads
fun requireTure(value: Boolean, detail: String? = null, code: CommonCode = CommonCode.FAIL) {
    if (!value) {
        fail(detail, code)
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun requireFalse(detail: String? = null, code: CommonCode = CommonCode.FAIL, block: () -> Boolean) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (block()) {
        fail(detail, code)
    }
}

@JvmOverloads
fun requireFalse(value: Boolean, detail: String? = null, code: CommonCode = CommonCode.FAIL) {
    if (value) {
        fail(detail, code)
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNull(detail: String? = null, code: CommonCode = CommonCode.FAIL, block: () -> T) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (!Objects.isNull(block())) {
        fail(detail, code)
    }
}

@JvmOverloads
fun <T> requireNull(value: T, detail: String? = null, code: CommonCode = CommonCode.FAIL) {
    if (!Objects.isNull(value)) {
        fail(detail, code)
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNonNull(detail: String? = null, code: CommonCode = CommonCode.FAIL, block: () -> T) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (Objects.isNull(block())) {
        fail(detail, code)
    }
}

@JvmOverloads
@OptIn(ExperimentalContracts::class)
fun <T> requireNonNull(value: T, detail: String? = null, code: CommonCode = CommonCode.FAIL) {
    contract {
        returns() implies (value != null)
    }
    if (Objects.isNull(value)) {
        fail(detail, code)
    }
}

@JvmOverloads
fun failWith(detail: String? = null, code: CommonCode = CommonCode.FAIL): BusinessException {
    return BusinessException(code, detail)
}