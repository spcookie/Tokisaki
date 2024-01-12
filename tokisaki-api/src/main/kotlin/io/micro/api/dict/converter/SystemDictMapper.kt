package io.micro.api.dict.converter

import io.micro.server.dict.domain.model.valobj.SystemDictEnum
import jakarta.inject.Singleton

@Singleton
class SystemDictMapper {

    fun intType2EnumType(type: Int): SystemDictEnum {
        return when (type) {
            0 -> SystemDictEnum.STRING
            1 -> SystemDictEnum.NUMBER
            2 -> SystemDictEnum.LIST
            3 -> SystemDictEnum.MAP
            else -> throw IllegalArgumentException("非法的字典参数类型")
        }
    }

    fun enumType2IntType(type: SystemDictEnum): Int {
        return when (type) {
            SystemDictEnum.STRING -> 0
            SystemDictEnum.NUMBER -> 1
            SystemDictEnum.LIST -> 2
            SystemDictEnum.MAP -> 3
            else -> throw IllegalArgumentException("非法的字典参数类型")
        }
    }

}