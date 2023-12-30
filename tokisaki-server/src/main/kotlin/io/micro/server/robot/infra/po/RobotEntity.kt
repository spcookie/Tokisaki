package io.micro.server.robot.infra.po

import io.micro.core.persistence.BaseEntity
import io.micro.server.auth.infra.po.User
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Table(name = "tokisaki_robot")
@Entity
class RobotEntity : BaseEntity() {

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @Column(name = "robot_name", nullable = false)
    var name: String? = null

    @Column(name = "robot_account", nullable = false)
    var account: String? = null

    @Column(name = "robot_password")
    var password: String? = null

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "robot_type", nullable = false)
    var type: Type? = null

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "robot_state", nullable = false)
    var state: State? = null

    @Column(name = "robot_remark")
    var remark: String? = null

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "use_function_id")
    var functions: MutableSet<UseFunctionEntity>? = null

    enum class Type { QQ }

    enum class State {
        Create,
        LoggingIn,
        LoggingFail,
        Online,
        Offline,
        Closed,
        ALL
    }

}