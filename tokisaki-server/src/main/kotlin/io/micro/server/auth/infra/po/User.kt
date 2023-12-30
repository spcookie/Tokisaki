package io.micro.server.auth.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.*

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Table(name = "tokisaki_user")
@Entity
class User : BaseEntity() {

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
    val authorities: MutableSet<Authority>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (name != other.name) return false
        if (openid != other.openid) return false
        if (authorities != other.authorities) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (openid?.hashCode() ?: 0)
        result = 31 * result + (authorities?.hashCode() ?: 0)
        return result
    }

}