package io.micro.server.robot.infra.po

import io.micro.core.persistence.BaseEntity
import io.micro.server.function.infra.po.FunctionEntity
import jakarta.persistence.*

@Table(name = "tokisaki_use_function")
@Entity
class UseFunctionEntity : BaseEntity() {

    @OneToOne
    @JoinColumn(name = "function_id")
    var functionPO: FunctionEntity? = null

    @Column(name = "use_function_remark")
    var remark: String? = null

    @Column(name = "use_function_enabled", nullable = false)
    var enabled: Boolean = false

}