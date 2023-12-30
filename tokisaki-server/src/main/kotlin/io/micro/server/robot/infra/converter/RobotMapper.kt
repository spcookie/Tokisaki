package io.micro.server.robot.infra.converter

import io.micro.server.function.infra.po.FunctionEntity
import io.micro.server.robot.domain.model.valobj.FeatureFunction
import io.micro.server.robot.infra.po.RobotEntity
import io.micro.server.robot.infra.po.UseFunctionEntity
import jakarta.inject.Singleton

@Singleton
class RobotMapper {

    fun number2Type(num: Int): RobotEntity.Type {
        val entries = RobotEntity.Type.entries
        if (num < entries.size) {
            return entries[num]
        } else {
            throw IllegalArgumentException()
        }
    }

    fun type2Number(type: RobotEntity.Type): Int {
        return RobotEntity.Type.entries.indexOf(type)
    }

    fun number2State(num: Int): RobotEntity.State {
        val entries = RobotEntity.State.entries
        if (num < entries.size) {
            return entries[num]
        } else {
            throw IllegalArgumentException()
        }
    }

    fun state2Number(state: RobotEntity.State): Int {
        return RobotEntity.State.entries.indexOf(state)
    }

    fun featureFunction2UseFunction(featureFunction: FeatureFunction): UseFunctionEntity {
        return UseFunctionEntity().apply {
            function = FunctionEntity().apply { id = featureFunction.id }
            remark = featureFunction.remark
            enabled = featureFunction.enabled
        }
    }

    fun useFunction2FeatureFunction(useFunctionPO: UseFunctionEntity): FeatureFunction {
        return FeatureFunction(
            id = useFunctionPO.id!!,
            remark = useFunctionPO.remark,
            enabled = useFunctionPO.enabled
        )
    }

}