package io.micro.core.context

import io.smallrye.common.vertx.ContextLocals

/**
 *@author Augenstern
 *@since 2023/10/20
 */
object AuthContext {

    object Constant {
        const val OPENID = "openid"
        const val NICKNAME = "nickname"
        const val ROLES = "roles"
    }

    fun hasRole(role: String): Boolean {
        return ContextLocals.get<Set<String>>(Constant.ROLES).orElse(setOf()).contains(role.lowercase())
    }

    val openid: String
        get() {
            return ContextLocals.get<String>(Constant.OPENID).get()
        }

    val nickname: String
        get() {
            return ContextLocals.get<String>(Constant.NICKNAME).get()
        }

    val roles: String
        get() {
            return ContextLocals.get<String>(Constant.ROLES).get()
        }

}