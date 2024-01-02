package io.micro.api.robot.dto

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "绑定功能信息")
@Serializable
class FeatureFunctionDTO {

    @Schema(name = "功能ID")
    @NotNull(message = "功能ID不能为空")
    var id: Long? = null

    @Schema(name = "名称")
    @NotNull(message = "功能名称不能为空")
    var name: String? = null

    @Schema(name = "备注")
    var remark: String? = null

    @Schema(name = "是否开启")
    var enabled: Boolean = false

}