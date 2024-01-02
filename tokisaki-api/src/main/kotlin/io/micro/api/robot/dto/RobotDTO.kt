package io.micro.api.robot.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "机器人信息")
@Serializable
class RobotDTO {

    @Schema(name = "用户ID")
    var userId: Long? = null

    @Schema(name = "机器人ID")
    @NotNull(groups = [ValidGroup.Update::class], message = "ID不能为空")
    var id: Long? = null

    @Schema(name = "名称")
    @NotNull(groups = [ValidGroup.Create::class], message = "名称不能为空")
    var name: String? = null

    @Schema(name = "帐号")
    @NotNull(groups = [ValidGroup.Create::class], message = "帐号不能为空")
    var account: String? = null

    @Schema(name = "密码")
    var password: String? = null

    @Schema(name = "类型")
    var type: Int? = null

    @Schema(name = "状态")
    var state: Int? = null

    @Schema(name = "备注")
    var remark: String? = null

    @Schema(name = "功能列表")
    val functions: MutableList<@Valid FeatureFunctionDTO> = mutableListOf()

}