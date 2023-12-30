package io.micro.api.robot.dto

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

@Serializable
data class FeatureFunctionDTO(

    @NotNull(message = "功能ID不能为空")
    var id: Long,

    var name: String? = null,

    var remark: String? = null,

    var enabled: Boolean = false

)