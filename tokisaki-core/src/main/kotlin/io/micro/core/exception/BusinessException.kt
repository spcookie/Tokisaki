package io.micro.core.exception

import io.micro.core.rest.CommonCode

open class BusinessException(
    val code: CommonCode = CommonCode.IGNORE,
    val detail: String? = null,
    cause: Throwable? = null
) : RuntimeException(detail, cause)