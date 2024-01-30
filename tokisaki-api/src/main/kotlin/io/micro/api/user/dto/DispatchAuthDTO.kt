package io.micro.api.user.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "分配权限")
@Serializable
class DispatchAuthDTO {

    @Schema(title = "机器人ID")
    var id: Long? = null

    @Schema(title = "添加权限")
    var addAuths: List<Long> = listOf()

    @Schema(title = "移除权限")
    var removeAuths: List<Long> = listOf()

}