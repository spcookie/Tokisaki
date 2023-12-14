package io.micro.api.wx.dto

import jakarta.ws.rs.QueryParam

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class AccessInfo {
    @QueryParam("signature")
    var signature: String? = null

    @QueryParam("timestamp")
    var timestamp: String? = null

    @QueryParam("nonce")
    var nonce: String? = null

    @QueryParam("echostr")
    var echostr: String? = null
}