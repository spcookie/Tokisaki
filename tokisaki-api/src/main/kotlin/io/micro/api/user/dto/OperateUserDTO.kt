package io.micro.api.user.dto

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "操作用户")
@Serializable
class OperateUserDTO {

    @NotNull
    @Schema(title = "用户ID")
    var id: Long? = null

    @NotNull
    @Schema(title = "用户名")
    var name: String? = null

}