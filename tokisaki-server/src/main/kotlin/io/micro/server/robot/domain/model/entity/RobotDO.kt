package io.micro.server.robot.domain.model.entity

import io.micro.core.entity.BaseDomainEntity
import io.micro.core.exception.requireNonNull
import io.micro.core.exception.requireTure
import io.micro.core.function.sdk.Cmd
import io.micro.core.rest.CommonCode

class RobotDO : BaseDomainEntity() {

    var userId: Long? = null

    var name: String? = null

    var account: String? = null

    var password: String? = null

    var type: Int? = null

    var state: Int? = null

    var remark: String? = null

    val functions: MutableList<FeatureFunctionDO> = mutableListOf()

    var cmdPrefix: String? = null

    private fun isValidType() = validTypeIds.contains(type)

    private fun isValidState() = validStateIds.contains(state)

    companion object {
        val validTypeIds = listOf(0)
        val validStateIds = listOf(0, 1, 2, 3, 4, 5, 6, 7)
        const val SPACE = " "
    }

    fun paramVerify() {
        requireTure("非法的机器人类型", CommonCode.ILLEGAL_STATE, ::isValidType)
        requireTure("非法的机器人状态", CommonCode.ILLEGAL_STATE, ::isValidState)
    }

    fun ensureCmdPrefix() {
        if (cmdPrefix == null) {
            cmdPrefix = "/"
        }
    }

    fun resolveCommand(groupId: Long, memberId: Long, text: String): FeatureFunctionDO? {
        val prefix = cmdPrefix
        requireNonNull(prefix)
        return if (text.startsWith(prefix)) {
            val nonPrefix = text.removePrefix(prefix)
            val args = nonPrefix.split(SPACE).toMutableList()
            val cmd = args.removeFirst()
            val cmdName = cmd.lowercase()
            val cmdEnum = Cmd.entries.associateBy { it.cmd }[cmdName]
            if (cmdEnum != null) {
                functions.filter { it.enabled }
                    .associateBy { it.code }[cmdEnum.code]
                    ?.also {
                        it.groupId = groupId
                        it.memberId = memberId
                        it.cmd = cmdEnum
                        it.args += args
                    }
            } else {
                null
            }
        } else {
            null
        }
    }

}