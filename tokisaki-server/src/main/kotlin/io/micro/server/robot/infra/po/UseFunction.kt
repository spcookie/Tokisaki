package io.micro.server.robot.infra.po

import io.micro.core.persistence.BasePO
import io.micro.server.function.infra.po.Function
import jakarta.persistence.*

@Table(name = "tokisaki_use_function")
@Entity
class UseFunction : BasePO() {

    @OneToOne
    @JoinColumn(name = "function_id")
    var function: Function? = null

    @Column(name = "use_function_remark")
    var remark: String? = null

    @Column(name = "use_function_enabled", nullable = false)
    var enabled: Boolean = false

}