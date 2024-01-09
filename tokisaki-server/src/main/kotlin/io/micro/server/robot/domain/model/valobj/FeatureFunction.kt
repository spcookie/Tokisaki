package io.micro.server.robot.domain.model.valobj

data class FeatureFunction(

    var id: Long? = null,

    var refId: Long? = null,

    var code: String? = null,

    var name: String? = null,

    var config: String? = null,

    var remark: String? = null,

    var enabled: Boolean = false

)
