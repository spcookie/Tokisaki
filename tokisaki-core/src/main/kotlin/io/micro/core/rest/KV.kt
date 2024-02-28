package io.micro.core.rest

import kotlinx.serialization.Serializable

@Serializable
class KV<V> {
    var key: String? = null
    var value: V? = null


//
//    fun <T : BaseEnum<V>> KVToBaseEnum(kv: KV<V>, @TargetType clazz: Class<T>): T  {
//        return clazz.getDeclaredConstructor().newInstance().apply {
//            require(kv.value != null, )
//            code = kv.value
//
//        }
//    }
}