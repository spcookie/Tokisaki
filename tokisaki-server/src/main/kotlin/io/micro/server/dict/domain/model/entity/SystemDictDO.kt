package io.micro.server.dict.domain.model.entity

import io.micro.core.entity.BaseDomainEntity
import io.micro.server.dict.domain.model.valobj.SystemDictEnum

class SystemDictDO : BaseDomainEntity() {

    var key: String? = null

    var value: String? = null

    var type: SystemDictEnum? = null

}