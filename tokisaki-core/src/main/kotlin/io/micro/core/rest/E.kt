package io.micro.core.rest

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/12/19
 */
@Serializable
data class E(
    var code: Int = CommonCode.IGNORE.code,
    var message: String = CommonCode.IGNORE.msg,
    var detail: String = "",
)