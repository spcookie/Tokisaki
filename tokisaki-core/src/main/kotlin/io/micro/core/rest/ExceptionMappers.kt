package io.micro.core.rest

import io.micro.core.exception.BusinessException
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.Priorities
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ExceptionMappers {

    @ServerExceptionMapper(BusinessException::class, priority = Priorities.USER - 1)
    fun businessExceptionMapper(ex: BusinessException): Uni<RestResponse<E>> {
        return Uni.createFrom().item { RestResponse.ok(E(Code.FAIL, ex.message.toString())) }
    }

    @ServerExceptionMapper(Exception::class)
    fun exceptionMapper(ex: Exception): Uni<RestResponse<E>> {
        return Uni.createFrom()
            .item {
                RestResponse.status(
                    RestResponse.Status.INTERNAL_SERVER_ERROR,
                    E(Code.ERROR, ex.message.toString())
                )
            }
    }

}