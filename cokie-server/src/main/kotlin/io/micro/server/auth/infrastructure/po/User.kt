package io.micro.server.auth.infrastructure.po

import io.micro.core.po.BasePO
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import jakarta.persistence.*

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@Table(name = "cokie_user")
@Entity
class User : BasePO() {

    companion object : PanacheCompanion<User>

    @Column(name = "user_name", nullable = false)
    var name: String? = null

    @Column(name = "user_openid", nullable = false)
    var openid: String? = null

    @JoinTable(
        name = "cokie_user_authority",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_id")]
    )
    @ManyToMany
    val authorities: MutableSet<Authority>? = null
}