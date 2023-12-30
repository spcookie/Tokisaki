package io.micro.server.auth.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Table(name = "tokisaki_user")
@Entity
class UserEntity : BaseEntity() {

    @Column(name = "user_name", nullable = false)
    var name: String? = null

    @Column(name = "user_openid", nullable = false)
    var openid: String? = null

    @JoinTable(
        name = "tokisaki_user_authority",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    @ManyToMany(fetch = FetchType.EAGER)
    val authorities: MutableSet<AuthorityEntity>? = null

    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as UserEntity

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

}