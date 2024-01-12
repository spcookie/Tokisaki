package io.micro.api.user.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "用户")
@Serializable
class QueryUserDTO {

    @Schema(title = "用户ID")
    var id: Long? = null

    @Schema(title = "用户名")
    var name: String? = null

    @Schema(title = "权限集合")
    val authorities: MutableSet<QueryAuthorityDTO> = mutableSetOf()

}