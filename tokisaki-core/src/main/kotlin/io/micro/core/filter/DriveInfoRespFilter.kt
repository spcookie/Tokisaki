package io.micro.core.filter

import io.vertx.core.http.Cookie.cookie
import io.vertx.core.http.HttpServerResponse
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.ext.Provider

/**
 *@author Augenstern
 *@since 2023/11/26
 */
@Provider
class DriveInfoRespFilter : ContainerResponseFilter {

    @Inject
    lateinit var reqInfo: ReqInfo

    @Context
    lateinit var response: HttpServerResponse

    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        val driveId = "driveId"
        val cookie = requestContext.cookies[driveId]
        responseContext.cookies
        if (cookie == null) {
            response.addCookie(cookie(driveId, reqInfo.driveId))
        }
    }
}