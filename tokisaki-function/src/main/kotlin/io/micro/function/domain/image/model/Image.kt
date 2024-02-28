package io.micro.function.domain.image.model

import io.micro.core.base.BaseDomainEntity
import java.util.*

/**
 *@author Augenstern
 *@since 2023/10/11
 */
abstract class Image : BaseDomainEntity() {

    lateinit var image: ByteArray

    lateinit var path: String

    fun generatePath() {
        path = UUID.randomUUID().toString()
    }

}