package io.micro.core.annotation

import jakarta.interceptor.InterceptorBinding

/**
 *@author Augenstern
 *@since 2023/10/15
 */
@InterceptorBinding
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.CONSTRUCTOR)
annotation class InitAuthContext
