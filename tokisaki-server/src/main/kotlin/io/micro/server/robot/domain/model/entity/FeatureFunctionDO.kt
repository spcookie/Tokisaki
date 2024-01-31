package io.micro.server.robot.domain.model.entity

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.micro.core.exception.requireNonNull
import io.micro.core.function.sdk.Cmd
import io.micro.core.rest.CommonCode
import io.micro.server.robot.domain.model.valobj.Switch

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

    var title: String? = null

    var cmd: Cmd? = null

    var args: MutableList<String> = mutableListOf()

    var switch: Switch = Switch()

    var groupId: Long? = null

    var memberId: Long? = null

    var isDescription: Boolean = false

    var isUndefined: Boolean = false

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

    fun ensureTitle(cmdCode: String) {
        if (title == null) {
            val cmd = Cmd.byCode(cmdCode)
            requireNonNull(cmd, code = CommonCode.NOT_FOUND)
            title = cmd.title
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
