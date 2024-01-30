package io.micro.server.auth.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class UserDO : BaseDomainEntity() {

    var name: String? = null

    val authorities: MutableSet<AuthorityDO> = mutableSetOf()

    var addAuths: MutableList<Long> = mutableListOf()

    var removeAuths: MutableList<Long> = mutableListOf()

    fun dispatchAuth() {
        val list = authorities.map(AuthorityDO::id).toMutableList()
        addAuths.removeAll(list)
        list += addAuths
        removeAuths.retainAll(list)

        val newAuthorities = addAuths.map { AuthorityDO().apply { id = it } }

        val iterator = authorities.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (removeAuths.contains(next.id)) {
                iterator.remove()
            }
        }

        authorities += newAuthorities
    }

}