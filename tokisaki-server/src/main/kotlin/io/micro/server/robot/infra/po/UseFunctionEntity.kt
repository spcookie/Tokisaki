package io.micro.server.robot.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "tokisaki_use_function")
@Entity
class UseFunctionEntity : BaseEntity() {

    @ManyToOne
    @JoinColumn(name = "function_id")
    var function: FunctionEntity? = null

    @ManyToOne
    var robot: RobotEntity? = null

    @Column(name = "use_function_config")
    var config: String? = null

    @Column(name = "use_function_remark")
    var remark: String? = null

    @Column(name = "use_function_enabled", nullable = false)
    var enabled: Boolean = false

    @Column(name = "use_function_cmd_alias")
    var cmdAlias: String? = null

}