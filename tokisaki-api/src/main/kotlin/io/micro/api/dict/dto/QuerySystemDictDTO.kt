package io.micro.api.dict.dto

import kotlinx.serialization.Serializable

@Serializable
class QuerySystemDictDTO {

    var id: Long? = null

    var key: String? = null

    var value: String? = null

    var type: Int? = null

}