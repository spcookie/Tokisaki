package io.micro.core.exception

open class BusinessException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)