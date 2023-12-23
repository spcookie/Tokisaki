package io.micro.server.robot.domain.model.entity

import io.micro.core.entity.BaseDomainEntity
import io.micro.core.exception.Throws
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import kotlin.properties.Delegates

class RobotManager : BaseDomainEntity() {

    var userId: Long? = null

    lateinit var name: String

    lateinit var account: String

    var authorization: String? = null

    var type: Int by Delegates.notNull()

    var state: Int = 0

    var remark: String? = null

    val functions: MutableList<FeatureFunction> = mutableListOf()

    private fun isValidType() = validTypeIds.contains(type)

    private fun isValidState() = validStateIds.contains(state)

    fun paramVerify() {
        Throws.requireNonNull(userId, "用户ID为空")
        Throws.requireTure("机器人账号为空") { ::account.isInitialized }
        Throws.requireTure("非法的机器人类型", ::isValidType)
        Throws.requireTure("非法的机器人状态", ::isValidState)
    }

    companion object {
        val validTypeIds = listOf(0)
        val validStateIds = listOf(0, 1, 2, 3, 4, 5, 6)
    }

}