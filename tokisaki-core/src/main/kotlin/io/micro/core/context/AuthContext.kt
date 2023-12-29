package io.micro.core.context

import io.smallrye.common.vertx.ContextLocals
import java.util.*

/**
 *@author Augenstern
 *@since 2023/10/20
 */
object AuthContext {

    object Constant {
        const val ID = "id"
        const val OPENID = "openid"
        const val NICKNAME = "nickname"
        const val ROLES = "roles"
    }

    fun hasRole(role: String?): Boolean =
        role?.let {
            Optional.ofNullable(roles)
                .orElse(setOf())
                .contains(role.lowercase())
        } ?: false


    fun equalId(id: String?) = id.equals(this.id)

    fun equalId(id: Long?) = id.toString().equals(this.id)

    fun equalOpenid(openid: String?) = openid.equals(this.openid)

    fun equalNickname(nickname: String?) = nickname.equals(this.nickname)

    val id: String
        get() = ContextLocals.get<String>(Constant.ID).get()

    val openid: String
        get() = ContextLocals.get<String>(Constant.OPENID).get()

    val nickname: String
        get() = ContextLocals.get<String>(Constant.NICKNAME).get()

    val roles: Set<String>
        get() = ContextLocals.get<Set<String>>(Constant.ROLES).get()

}