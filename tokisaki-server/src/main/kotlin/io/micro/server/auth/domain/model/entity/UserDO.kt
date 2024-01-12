package io.micro.server.auth.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class UserDO : BaseDomainEntity() {

    var name: String? = null

    val authorities: MutableSet<AuthorityDO> = mutableSetOf()

}