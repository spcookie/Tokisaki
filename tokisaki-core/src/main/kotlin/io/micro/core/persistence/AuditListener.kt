package io.micro.core.persistence

import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.util.*

/**
 *@author Augenstern
 *@since 2023/11/25
 */
class AuditListener {

    @PrePersist
    fun prePersist(entity: BaseEntity) {
        val date = Date()
        entity.createdAt = date
        entity.modifyAt = date
    }

    @PreUpdate
    fun preUpdate(entity: BaseEntity) {
        entity.modifyAt = Date()
    }
}