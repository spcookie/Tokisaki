package io.micro.core.filter

import cn.hutool.core.util.IdUtil
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.ext.Provider

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Provider
class DriveInfoReqFilter : ContainerRequestFilter {

    @Inject
    lateinit var reqInfo: ReqInfo

    override fun filter(requestContext: ContainerRequestContext) {
        val driveId = "driveId"
        val cookie = requestContext.cookies[driveId]
        reqInfo.driveId = if (cookie == null) {
            IdUtil.fastSimpleUUID()
        } else {
            cookie.value
        }
    }
}