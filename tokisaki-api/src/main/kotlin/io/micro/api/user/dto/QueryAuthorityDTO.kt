package io.micro.api.user.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "权限")
@Serializable
class QueryAuthorityDTO {

    @Schema(title = "权限ID")
    var id: Long? = null

    @Schema(title = "权限值")
    var value: String? = null

}