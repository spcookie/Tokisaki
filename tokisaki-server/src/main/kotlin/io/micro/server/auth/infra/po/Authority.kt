package io.micro.server.auth.infra.po

import io.micro.core.persistence.BasePO
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
class Authority : BasePO() {
    @Column(name = "authority_value")
    var value: String? = null

    @ManyToMany(mappedBy = "authorities")
    var users: MutableSet<User>? = null
}