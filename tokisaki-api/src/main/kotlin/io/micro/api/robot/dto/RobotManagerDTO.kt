package io.micro.api.robot.dto

import kotlinx.serialization.Serializable

@Serializable
data class RobotManagerDTO(
    var userId: Long,

    var name: String,

    var type: Int,

    var state: Int = 0,

    var remark: String? = null,

    val functions: MutableList<FeatureFunctionDTO> = mutableListOf()
)