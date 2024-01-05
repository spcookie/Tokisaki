package io.micro.core.rest

import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/11/24
 */
@Serializable
data class R<T>(
    val code: Int = CommonCode.OK.code,
    var message: String = "",
    var data: T? = null
) {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun <E> newInstance(msg: String = CommonCode.OK.msg, data: E? = null): R<E> {
            return R(message = msg, data = data)
        }
    }
}