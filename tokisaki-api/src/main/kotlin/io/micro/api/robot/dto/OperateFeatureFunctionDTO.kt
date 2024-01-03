package io.micro.api.robot.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "绑定功能信息")
@Serializable
class OperateFeatureFunctionDTO {

    @Schema(title = "功能ID")
    @NotNull(groups = [ValidGroup.Create::class, ValidGroup.Update::class], message = "功能ID不能为空")
    var id: Long? = null

    @Schema(title = "备注")
    var remark: String? = null

    @Schema(title = "是否开启")
    var enabled: Boolean = false

}