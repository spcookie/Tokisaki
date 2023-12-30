package io.micro.api.robot.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class RobotManagerDTO(
    var userId: Long? = null,

    @NotNull(groups = [ValidGroup.Update::class], message = "ID不能为空")
    var id: Long? = null,

    @NotNull(groups = [ValidGroup.Create::class], message = "名称不能为空")
    var name: String? = null,

    @NotNull(groups = [ValidGroup.Create::class], message = "帐号不能为空")
    var account: String? = null,

    var password: String? = null,

    @NotNull(groups = [ValidGroup.Create::class], message = "类型不能为空")
    var type: Int = 0,

    var state: Int = 6,

    var remark: String? = null,

    val functions: MutableList<@Valid FeatureFunctionDTO> = mutableListOf()
)