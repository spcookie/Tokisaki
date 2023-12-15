package io.micro.core.context

import io.smallrye.common.vertx.ContextLocals

/**
 *@author Augenstern
 *@since 2023/10/20
 */
object AuthContext {
    object Constant {
        const val USER = "user"
        const val TENANT = "tenant"
        const val CLIENT = "client"
        const val ROLES = "roles"
    }

    fun hasRole(role: String): Boolean {
        return ContextLocals.get<Set<String>>(Constant.ROLES).orElse(setOf()).contains(role.lowercase())
    }

    val user: String
        get() {
            return ContextLocals.get<String>(Constant.USER).get()
        }

    val tenant: String
        get() {
            return ContextLocals.get<String>(Constant.TENANT).get()
        }

    val client: String
        get() {
            return ContextLocals.get<String>(Constant.CLIENT).get()
        }
}