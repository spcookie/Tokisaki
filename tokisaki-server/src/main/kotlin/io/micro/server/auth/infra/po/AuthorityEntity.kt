package io.micro.server.auth.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Table(name = "tokisaki_authority")
@Entity
class AuthorityEntity : BaseEntity() {

    @Column(name = "authority_value", nullable = false)
    var value: String? = null

    @ManyToMany(mappedBy = "authorities")
    var users: MutableSet<UserEntity>? = null

    @Column(name = "authority_enabled", nullable = false)
    var enabled: Boolean = true

    @Column(name = "authority_remark")
    var remark: String? = null

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as AuthorityEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

}