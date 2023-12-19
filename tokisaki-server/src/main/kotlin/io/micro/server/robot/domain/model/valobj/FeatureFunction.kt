package io.micro.server.robot.domain.model.valobj

data class FeatureFunction(
    var id: Long,
    var remark: String? = null,
    var enabled: Boolean = false
)
