package io.micro.server.robot.domain.model.entity

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.micro.core.exception.requireNonNull
import io.micro.core.function.sdk.Cmd
import io.micro.core.rest.CommonCode
import io.micro.server.robot.domain.model.valobj.Switch
import kotlin.properties.Delegates

class FeatureFunctionDO {

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

    companion object {
        const val BRACES = "{}"
    }

    fun ensureCmdAlias(cmdCode: String) {
        if (cmdAlias == null) {
            val cmd = Cmd.byCode(cmdCode)
            requireNonNull(cmd, code = CommonCode.NOT_FOUND)
            cmdAlias = cmd.cmd
        }
    }

    fun switched(): Boolean {
        val (enableGroups, disableGroups, enableMembers, disableMembers) = switch
        return if (disableGroups.isNotEmpty()) {
            if (!disableGroups.contains(groupId)) {
                if (disableMembers.isNotEmpty()) {
                    !disableMembers.contains(memberId)
                } else {
                    enableMembers.contains(memberId)
                }
            } else {
                false
            }
        } else {
            if (enableGroups.contains(groupId)) {
                if (disableMembers.isNotEmpty()) {
                    !disableMembers.contains(memberId)
                } else {
                    enableMembers.contains(memberId)
                }
            } else {
                false
            }
        }
    }

    fun getConfigMap(objectMapper: ObjectMapper): Map<String, *> {
        val configStr = config ?: BRACES
        return objectMapper.readValue(configStr, object : TypeReference<Map<String, *>>() {})
    }

}
