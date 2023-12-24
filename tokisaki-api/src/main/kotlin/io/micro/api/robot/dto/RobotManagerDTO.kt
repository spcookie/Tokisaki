package io.micro.api.robot.dto

import kotlinx.serialization.Serializable

@Serializable
data class RobotManagerDTO(
    var userId: Long,

    var id: Long? = null,

    var name: String,

    var account: String,

    var authorization: String? = null,

    var type: Int,

    var state: Int = 0,

    var remark: String? = null,

    val functions: MutableList<FeatureFunctionDTO> = mutableListOf()
)