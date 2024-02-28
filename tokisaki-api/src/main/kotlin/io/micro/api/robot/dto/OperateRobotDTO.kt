package io.micro.api.robot.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "机器人信息")
@Serializable
class OperateRobotDTO {

    @Schema(title = "机器人ID")
    @NotNull(groups = [ValidGroup.Update::class], message = "ID不能为空")
    var id: Long? = null

    @Schema(title = "名称")
    @NotNull(groups = [ValidGroup.Create::class], message = "名称不能为空")
    var name: String? = null

    @Schema(title = "帐号")
    @NotNull(groups = [ValidGroup.Create::class], message = "帐号不能为空")
    var account: String? = null

    @Schema(title = "密码")
    var password: String? = null

    @Schema(title = "类型")
    var type: String? = null

    @Schema(title = "状态")
    var state: String? = null

    @Schema(title = "备注")
    var remark: String? = null

}