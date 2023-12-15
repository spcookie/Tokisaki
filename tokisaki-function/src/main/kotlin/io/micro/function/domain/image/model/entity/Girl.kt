package io.micro.function.domain.image.model.entity

import io.micro.core.fundto.Cmd
import io.micro.core.funsdk.AbstractCmd
import io.micro.function.domain.image.model.Image
import io.net.spcokie.types.QQFace
import io.smallrye.mutiny.Uni
import kotlin.random.Random
import kotlin.random.nextInt

/**
 *@author Augenstern
 *@since 2023/10/8
 */
class Girl : Image() {
    companion object : AbstractCmd() {

        private val modes = arrayOf("1", "3", "5", "7", "8", "9")

        fun obtainMode(): String {
            return modes[Random.nextInt(0..5)]
        }

        @JvmStatic
        override fun identify() = Cmd.Girl

        @JvmStatic
        override fun describe(call: Long): Uni<String> {
            return Uni.createFrom().item {
                """
                ${QQFace.BIAN_PAO}随机小姐姐 ◟$call◞
                    ╰g()
            """.trimIndent()
            }
        }

    }
}