package io.micro.server.robot.domain.model.entity

import io.micro.core.entity.BaseDomainEntity
import io.micro.core.exception.Throws
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import kotlin.properties.Delegates

class RobotManager : BaseDomainEntity() {

    var userId: Long by Delegates.notNull()

    lateinit var name: String

    var type: Int by Delegates.notNull()

    var state: Int = 0

    var remark: String? = null

    val functions: MutableList<FeatureFunction> = mutableListOf()

    fun isValidType() = validTypeIds.contains(type)

    fun isValidState() = validStateIds.contains(state)

    fun verify() {
        Throws.failIfFalse(::isValidType, "非法的类型")
        Throws.failIfFalse(::isValidState, "非法的状态")
    }

    companion object {
        val validTypeIds = listOf(0)
        val validStateIds = listOf(0, 1, 2, 3, 4, 5, 6)
    }

}