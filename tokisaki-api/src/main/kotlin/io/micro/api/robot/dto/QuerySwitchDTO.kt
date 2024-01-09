package io.micro.api.robot.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "机器人功能权限开关")
@Serializable
class QuerySwitchDTO {

    @Schema(title = "启用的分组")
    var enableGroups: MutableSet<Long> = mutableSetOf()

    @Schema(title = "禁用的分组")
    var disableGroups: MutableSet<Long> = mutableSetOf()

    @Schema(title = "启用的成员")
    var enableMembers: MutableSet<Long> = mutableSetOf()

    @Schema(title = "禁用的成员")
    var disableMembers: MutableSet<Long> = mutableSetOf()

}