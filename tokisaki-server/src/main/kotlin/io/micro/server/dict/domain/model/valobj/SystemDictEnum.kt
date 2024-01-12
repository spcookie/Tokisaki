package io.micro.server.dict.domain.model.valobj

import kotlin.reflect.KClass

enum class SystemDictEnum(val type: KClass<*>) {
    STRING(String::class),
    NUMBER(Long::class),
    LIST(List::class),
    MAP(Map::class)
}