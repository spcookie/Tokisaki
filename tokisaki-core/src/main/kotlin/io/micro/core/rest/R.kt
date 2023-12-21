package io.micro.core.rest

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/11/24
 */
@Serializable
data class R<T>(
    var code: Int = Code.OK,
    var message: String = "",
    var data: T? = null
) {
    companion object {
        fun <E> newInstance(msg: String = "", data: E? = null): R<E> {
            return R(message = msg, data = data)
        }
    }
}