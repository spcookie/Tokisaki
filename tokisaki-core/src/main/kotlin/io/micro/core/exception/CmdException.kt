package io.net.spcokie.common.exception

import jakarta.validation.ConstraintViolationException

/**
 *@author Augenstern
 *@since 2023/10/16
 */
class CmdException(message: String? = null) : RuntimeException(message) {
    companion object {
        fun fail(msg: String): Nothing {
            throw CmdException(msg)
        }

        fun failForUni(msg: String): CmdException {
            return CmdException(msg)
        }

        fun nonImplemented(): Nothing {
            throw CmdException("Not yet implemented")
        }

        fun fail(ex: ConstraintViolationException) {
            val msg = ex.constraintViolations.joinToString(",") { it.message }
            throw CmdException(msg)
        }
    }
}