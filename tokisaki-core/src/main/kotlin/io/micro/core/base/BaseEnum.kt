package io.micro.core.base

interface BaseEnum<T> {
    var title: String
    var code: T

    fun eq(baseEnum: BaseEnum<*>): Boolean {
        return baseEnum.code == this.code
    }

    fun eq(code: T): Boolean {
        return code == this.code
    }

}