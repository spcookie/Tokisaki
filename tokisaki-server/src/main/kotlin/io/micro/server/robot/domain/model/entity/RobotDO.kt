package io.micro.server.robot.domain.model.entity

import io.micro.core.base.BaseDomainEntity
import io.micro.core.base.BaseEnum
import io.micro.core.exception.requireNonNull
import io.micro.core.exception.requireTure
import io.micro.core.function.sdk.Cmd
import io.micro.core.rest.CommonCode

class RobotDO : BaseDomainEntity() {

    var userId: Long? = null

    var name: String? = null

    var account: String? = null

    var password: String? = null

    var type: Type? = null

    var state: State? = null

    var remark: String? = null

    val functions: MutableList<FeatureFunctionDO> = mutableListOf()

    var cmdPrefix: String? = null

    private fun isValidType() = type?.let { Type.query(it.code) != null } ?: false

    private fun isValidState() = state?.let { State.query(it.code) != null } ?: false

    enum class Type(override var title: String, override var code: String) : BaseEnum<String> {
        QQ("逆向QQ机器人", "113.01");

        companion object {
            fun query(code: String): Type? {
                return entries.associateBy { it.code }.get(code)
            }
        }
    }

    enum class State(override var title: String, override var code: String) : BaseEnum<String> {
        Create("已创建", "114.01"),
        LoggingIn("登录中", "114.02"),
        LoggingFail("登录失败", "114.03"),
        Online("在线", "114.04"),
        Offline("离线", "114.05"),
        Closed("已关闭", "114.06"),
        ALL("ALL", "114.07");

        companion object {
            fun query(code: String): State? {
                return entries.associateBy { it.code }.get(code)
            }
        }
    }

    companion object {
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
                FeatureFunctionDO().also { it.isUndefined = true }
            }
        } else {
            null
        }
    }

}