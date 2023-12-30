package io.micro.api.function.dto

import kotlinx.serialization.Serializable

@Serializable
class FunctionDTO {

    var id: Long? = null

    var name: String? = null

    var code: String? = null

    var config: String? = null

}