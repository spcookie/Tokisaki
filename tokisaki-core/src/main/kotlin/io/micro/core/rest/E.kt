package io.micro.core.rest

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/12/19
 */
@Serializable
data class E(
    var code: Int = Code.IGNORE,
    var message: String = "",
)