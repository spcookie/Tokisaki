package io.micro.core.exception

class BusinessException(message: String? = null, cause: Throwable? = null) : RuntimeException(message, cause)