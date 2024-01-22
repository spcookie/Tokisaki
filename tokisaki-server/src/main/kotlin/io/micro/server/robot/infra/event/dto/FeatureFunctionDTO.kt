package io.micro.server.robot.infra.event.dto

import io.micro.core.function.sdk.Cmd
import io.micro.server.robot.domain.model.valobj.Switch
import kotlin.properties.Delegates

class FeatureFunctionDTO {

    var id: Long? = null

    var refId: Long? = null

    var code: String? = null

    var name: String? = null

    var config: String? = null

    var remark: String? = null

    var enabled: Boolean = false

    var cmdAlias: String? = null

    var requireQuota: Boolean? = null

    var cmd: Cmd by Delegates.notNull()

    var args: MutableList<String> = mutableListOf()

    var switch: Switch = Switch()

    var groupId: Long by Delegates.notNull()

    var memberId: Long by Delegates.notNull()

}
