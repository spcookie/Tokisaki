package io.micro.function.domain.image.service

import io.smallrye.mutiny.Uni


/**
 *@author Augenstern
 *@since 2023/10/11
 */
interface ImageTask {
    fun download(): Uni<Void>
    fun remove(): Uni<Void>
}