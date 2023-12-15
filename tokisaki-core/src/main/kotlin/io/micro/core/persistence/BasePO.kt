package io.micro.core.persistence

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.*
import java.util.*

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@EntityListeners(AuditListener::class)
@MappedSuperclass
abstract class BasePO : PanacheEntity() {

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.DATE)
    open var createdAt: Date? = null

    @Column(name = "modify_at", nullable = false)
    @Temporal(TemporalType.DATE)
    open var modifyAt: Date? = null
}