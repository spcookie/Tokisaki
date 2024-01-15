package io.micro.function.domain.image.model.entity

import io.micro.core.function.sdk.AbstractCmd
import io.micro.core.function.sdk.Cmd
import io.micro.function.domain.image.model.Image
import io.micro.function.types.QQFace
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/22
 */
class AnimeCartoon : Image() {

    companion object : AbstractCmd() {

        @JvmStatic
        override fun identify() = Cmd.Anime

        @JvmStatic
        override fun describe(call: Long): Uni<String> {
            return Uni.createFrom().item {
                """
                ${QQFace.SHI_ZI_TOU}随机动漫 ◟$call◞
                    ╰ac()
                """.trimIndent()
            }
        }

    }

}