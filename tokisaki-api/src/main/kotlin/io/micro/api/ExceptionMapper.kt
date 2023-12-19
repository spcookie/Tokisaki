package io.micro.api

import io.micro.core.exception.BusinessException
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.Priorities
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ExceptionMapper {

    @ServerExceptionMapper(BusinessException::class, priority = Priorities.USER - 1)
    fun businessExceptionMapper(ex: BusinessException): Uni<RestResponse<R<Unit>>> {
        return Uni.createFrom().item { RestResponse.ok(R.fail(ex.message.toString())) }
    }

    @ServerExceptionMapper(Exception::class)
    fun exceptionMapper(ex: Exception): Uni<RestResponse<R<Unit>>> {
        return Uni.createFrom()
            .item { RestResponse.status(RestResponse.Status.INTERNAL_SERVER_ERROR, R.error(ex.message.toString())) }
    }

}