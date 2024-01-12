package io.micro.server.robot.domain.model.entity

import io.micro.core.exception.requireNonNull
import io.micro.core.function.sdk.Cmd
import io.micro.core.rest.CommonCode

class FeatureFunctionDO {

    var id: Long? = null

    var refId: Long? = null

    var code: String? = null

    var name: String? = null

    var config: String? = null

    var remark: String? = null

    var enabled: Boolean = false

    var cmdAlias: String? = null

    fun ensureCmdAlias(cmdCode: String) {
        if (cmdAlias == null) {
            val cmd = Cmd.byCode(cmdCode)
            requireNonNull(cmd, code = CommonCode.NOT_FOUND)
            cmdAlias = cmd.cmd
        }
    }

}
