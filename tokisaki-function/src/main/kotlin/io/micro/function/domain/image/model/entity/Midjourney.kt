package io.micro.function.domain.image.model.entity

import io.micro.core.function.sdk.AbstractCmd
import io.micro.core.function.sdk.ArgsMergeStrategy
import io.micro.core.function.sdk.Cmd
import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.valobj.MidjourneyConfig
import io.net.spcokie.types.QQFace
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/19
 */
class Midjourney() : Image() {

    constructor(config: MidjourneyConfig, prompt: String) : this() {
        this.config = config
        this.prompt = prompt
    }

    lateinit var config: MidjourneyConfig

    lateinit var prompt: String

    companion object : AbstractCmd() {

        @JvmStatic
        override fun identify() = Cmd.Mj

        @JvmStatic
        override fun args(): ArgsMergeStrategy {
            return ArgsMergeStrategy.Merge
        }

        @JvmStatic
        override fun describe(call: Long): Uni<String> {
            return Uni.createFrom().item {
                """
                ${QQFace.DENG_LONG}绘图 ◟$call◞
                    ╰mj(prompt)
                      └prompt-提示词
            """.trimIndent()
            }
        }

        fun create(midjourneyConfig: MidjourneyConfig, prompt: String): Midjourney {
            return Midjourney(midjourneyConfig, prompt)
        }
    }
}