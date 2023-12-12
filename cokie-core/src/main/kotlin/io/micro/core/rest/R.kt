package io.micro.core.rest

/**
 *@author Augenstern
 *@since 2023/11/24
 */
data class R<T>(
    var code: Int = Code.IGNORE,
    var message: String = "",
    var data: T? = null
) {
    companion object {

        fun <E> success(msg: String, data: E): R<E> {
            return R<E>(Code.OK, msg, data)
        }

        fun success(): R<Unit> {
            return R(Code.OK)
        }

        fun success(msg: String): R<Unit> {
            return R(Code.OK, msg)
        }

        fun fail(msg: String): R<Unit> {
            return R(Code.FAIL, msg)
        }

        fun fail(): R<Unit> {
            return R(Code.FAIL)
        }
    }
}