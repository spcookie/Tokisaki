package io.micro.server.function.domain.model.entity

import io.micro.core.entity.BaseDomainEntity

class FunctionDO : BaseDomainEntity() {

    var code: String? = null

    var config: String? = null

}