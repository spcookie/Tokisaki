package io.micro.server.robot.infra.event.dto

import io.micro.core.base.BaseEnum

class RobotDTO {

    var id: Long? = null

    var userId: Long? = null

    var name: String? = null

    var account: String? = null

    var password: String? = null

    var type: Type? = null

    var state: State? = null

    var remark: String? = null

    val functions: MutableList<FeatureFunctionDTO> = mutableListOf()

    var cmdPrefix: String? = null

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

}