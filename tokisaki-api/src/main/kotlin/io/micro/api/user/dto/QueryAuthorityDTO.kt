package io.micro.api.user.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "查询权限")
@Serializable
class QueryAuthorityDTO {

    @Schema(title = "权限ID")
    var id: Long? = null

    @Schema(title = "权限值")
    var value: String? = null

    @Schema(title = "备注")
    var remark: String? = null

    @Schema(title = "是否启用")
    var enabled: Boolean? = null

}