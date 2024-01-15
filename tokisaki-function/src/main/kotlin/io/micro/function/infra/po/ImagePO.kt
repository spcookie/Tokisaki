package io.micro.function.infra.po

import io.micro.function.types.ImageCategory
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.Uni
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

/**
 *@author Augenstern
 *@since 2023/10/9
 */
@Table(name = "image")
@Entity
class ImagePO(
    var name: String? = null,

    @Enumerated(EnumType.STRING)
    var category: ImageCategory? = null,

    var path: String? = null,

    var url: String? = null,

    var used: Boolean? = null
) : PanacheEntity() {

    companion object : PanacheCompanion<ImagePO> {

        fun selectIsNotUsedByCategory(category: ImageCategory): Uni<ImagePO> {
            return find("category = ?1 and used = ?2", category, false).firstResult()
                .flatMap {
                    if (it == null) {
                        Uni.createFrom().nullItem()
                    } else {
                        Uni.createFrom().item(it)
                    }
                }
        }

        fun countUnusedByCategory(category: ImageCategory): Uni<Long> {
            return count("category = ?1 and used = ?2", category, false)
        }

        fun updateUsedById(id: Long): Uni<Int> {
            return update("used = ?1 where id = ?2", true, id)
        }

        fun selectImageUsedByCategory(category: ImageCategory): Uni<List<ImagePO>> {
            return find("category = ?1 and used = ?2", category, true).list()
        }

        fun deleteImageByIds(ids: List<Long>): Uni<Boolean> {
            return Uni.join()
                .all(ids.map { deleteById(it) })
                .andFailFast()
                .map { list ->
                    list.all { it }
                }
        }
    }

    fun persistAndFlushWithUnused(): Uni<ImagePO> {
        used = false
        return persistAndFlush()
    }
}