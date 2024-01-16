package io.micro.server.auth.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class AuthorityDO : BaseDomainEntity() {

    var value: String? = null

    var remark: String? = null

    var enabled: Boolean? = null
}