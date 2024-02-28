package io.micro.core.base

import io.micro.core.rest.KV
import jakarta.inject.Singleton
import org.mapstruct.TargetType

@Singleton
class BaseEnumConvertor {
    fun <T> baseEnumToKV(baseEnum: BaseEnum<T>): KV<T> {
        return KV<T>().apply {
            key = baseEnum.title
            value = baseEnum.code
        }
    }

    fun <T, R : BaseEnum<T>> codeToBaseEnum(code: T, @TargetType clazz: Class<R>): R? {
        for (enumConstant in clazz.enumConstants) {
            if (enumConstant.eq(code)) {
                return enumConstant
            }
        }
        return null
    }
}