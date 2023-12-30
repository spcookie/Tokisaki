package io.micro.server.function.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "tokisaki_function")
@Entity
class FunctionEntity : BaseEntity() {

    @Column(name = "function_config")
    var config: String? = null

}