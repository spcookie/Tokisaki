package io.micro.api.user.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "用户统计数据")
@Serializable
class UserStatisticsDTO {

    @Schema(title = "在线人数")
    var onlineUserCount: Int? = null

}