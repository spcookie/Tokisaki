package io.micro.server.function.infra.po

import io.micro.core.persistence.BasePO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "tokisaki_function")
@Entity
class Function : BasePO() {

    @Column(name = "function_config")
    var config: String? = null

}