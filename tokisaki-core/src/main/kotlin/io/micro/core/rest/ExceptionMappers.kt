package io.micro.core.rest

import io.micro.core.exception.BusinessException
import io.smallrye.mutiny.Uni
import jakarta.validation.ConstraintViolationException
import jakarta.ws.rs.Priorities
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ExceptionMappers {

    @ServerExceptionMapper(BusinessException::class, priority = Priorities.USER - 1)
    fun businessExceptionMapper(ex: BusinessException): Uni<RestResponse<E>> {
        return Uni.createFrom().item { RestResponse.ok(E(ex.code.code, ex.code.msg, ex.detail.toString())) }
    }

    @ServerExceptionMapper(ConstraintViolationException::class, priority = Priorities.USER - 1)
    fun constraintViolationExceptionMapper(ex: ConstraintViolationException): Uni<RestResponse<E>> {
        return Uni.createFrom()
            .item {
                RestResponse.status(
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    E(
                        CommonCode.ILLEGAL_PARAMETER.code,
                        CommonCode.ILLEGAL_PARAMETER.msg,
                        ex.constraintViolations.map { it.message }.joinToString()
                    )
                )
            }
    }

    @ServerExceptionMapper(Exception::class)
    fun exceptionMapper(ex: Exception): Uni<RestResponse<E>> {
        return Uni.createFrom()
            .item {
                RestResponse.status(
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    E(CommonCode.ERROR.code, CommonCode.ERROR.msg, ex.toString())
                )
            }
    }

}