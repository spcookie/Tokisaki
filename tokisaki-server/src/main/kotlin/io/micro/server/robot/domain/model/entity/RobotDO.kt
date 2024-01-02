package io.micro.server.robot.domain.model.entity

import io.micro.core.entity.BaseDomainEntity
import io.micro.core.exception.requireTure
import io.micro.core.rest.CommonCode
import io.micro.server.robot.domain.model.valobj.FeatureFunction

class RobotDO : BaseDomainEntity() {

    var userId: Long? = null

    var name: String? = null

    var account: String? = null

    var password: String? = null

    var type: Int? = null

    var state: Int? = null

    var remark: String? = null

    val functions: MutableList<FeatureFunction> = mutableListOf()

    private fun isValidType() = validTypeIds.contains(type)

    private fun isValidState() = validStateIds.contains(state)

    fun paramVerify() {
        requireTure("非法的机器人类型", CommonCode.ILLEGAL_STATE, ::isValidType)
        requireTure("非法的机器人状态", CommonCode.ILLEGAL_STATE, ::isValidState)
    }

    companion object {
        val validTypeIds = listOf(0)
        val validStateIds = listOf(0, 1, 2, 3, 4, 5, 6, 7)
    }

}