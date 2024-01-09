package io.micro.server.robot.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class FunctionDO : BaseDomainEntity() {

    var name: String? = null

    var code: String? = null

    var configHint: String? = null

}