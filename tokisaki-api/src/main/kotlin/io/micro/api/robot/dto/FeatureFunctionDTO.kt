package io.micro.api.robot.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeatureFunctionDTO(
    var id: Long,
    var remark: String? = null,
    var enabled: Boolean = false
)