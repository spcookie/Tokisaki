package io.micro.server.auth.infra.po

import io.micro.core.persistence.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Table(name = "tokisaki_authority")
@Entity
class Authority : BaseEntity() {

    @Column(name = "authority_value")
    var value: String? = null

    @ManyToMany(mappedBy = "authorities")
    var users: MutableSet<User>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Authority) return false

        if (value != other.value) return false
        if (users != other.users) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + (users?.hashCode() ?: 0)
        return result
    }

}