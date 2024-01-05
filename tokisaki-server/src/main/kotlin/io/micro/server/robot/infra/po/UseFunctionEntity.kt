package io.micro.server.robot.infra.po

import io.micro.core.persistence.BaseEntity
import io.micro.server.function.infra.po.FunctionEntity
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Table(name = "tokisaki_use_function")
@Entity
class UseFunctionEntity : BaseEntity() {

    @ManyToOne
    @JoinColumn(name = "function_id")
    var function: FunctionEntity? = null

    @ManyToOne
    var robot: RobotEntity? = null

    @Column(name = "use_function_remark")
    var remark: String? = null

    @Column(name = "use_function_enabled", nullable = false)
    var enabled: Boolean = false

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as UseFunctionEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

}