package io.micro.server.auth.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class UserStatisticsDO : BaseDomainEntity() {

    var onlineUserCount: Int? = null

    var enrollUserCount: Long? = null

}