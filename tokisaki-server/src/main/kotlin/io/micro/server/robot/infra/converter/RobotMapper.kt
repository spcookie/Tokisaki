package io.micro.server.robot.infra.converter

import io.micro.server.function.infra.po.Function
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import io.micro.server.robot.infra.po.Robot
import io.micro.server.robot.infra.po.UseFunction
import jakarta.inject.Singleton

@Singleton
class RobotMapper {

    fun number2Type(num: Int): Robot.Type {
        val entries = Robot.Type.entries
        if (num < entries.size) {
            return entries[num]
        } else {
            throw IllegalArgumentException()
        }
    }

    fun type2Number(type: Robot.Type): Int {
        return Robot.Type.entries.indexOf(type)
    }

    fun number2State(num: Int): Robot.State {
        val entries = Robot.State.entries
        if (num < entries.size) {
            return entries[num]
        } else {
            throw IllegalArgumentException()
        }
    }

    fun state2Number(state: Robot.State): Int {
        return Robot.State.entries.indexOf(state)
    }

    fun featureFunction2UseFunction(featureFunction: FeatureFunction): UseFunction {
        return UseFunction().apply {
            function = Function().apply { id = featureFunction.id }
            remark = featureFunction.remark
            enabled = featureFunction.enabled
        }
    }

    fun useFunction2FeatureFunction(useFunction: UseFunction): FeatureFunction {
        return FeatureFunction(
            id = useFunction.id!!,
            remark = useFunction.remark,
            enabled = useFunction.enabled
        )
    }

}