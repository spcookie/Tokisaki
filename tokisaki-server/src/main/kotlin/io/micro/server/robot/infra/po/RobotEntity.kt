package io.micro.server.robot.infra.po

import io.micro.core.persistence.BaseEntity
import io.micro.server.auth.infra.po.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.proxy.HibernateProxy

@Table(name = "tokisaki_robot")
@Entity
class RobotEntity : BaseEntity() {

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

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

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinColumn(name = "robot_id")
    var functions: MutableSet<UseFunctionEntity>? = null

//    @Column(name = "robot_cmd_prefix", nullable = false)
//    var cmdPrefix: String? = null

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

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as RobotEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

}