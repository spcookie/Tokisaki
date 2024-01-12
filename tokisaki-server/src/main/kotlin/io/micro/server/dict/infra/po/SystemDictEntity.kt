package io.micro.server.dict.infra.po

import io.micro.core.persistence.BaseEntity
import io.micro.server.dict.domain.model.valobj.SystemDictEnum
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "tokisaki_system_dict")
@Entity
class SystemDictEntity : BaseEntity() {

    @Column(name = "system_dict_key", nullable = false)
    var key: String? = null

    @Column(name = "system_dict_value", nullable = false)
    var value: String? = null

    @Column(name = "system_dict_type", nullable = false)
    var type: SystemDictEnum? = null

    enum class SystemDictEnum { STRING, NUMBER, LIST, MAP }

}