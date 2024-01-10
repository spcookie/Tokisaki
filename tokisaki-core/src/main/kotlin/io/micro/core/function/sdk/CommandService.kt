package io.micro.core.function.sdk

import io.micro.core.function.ConfigHint
import io.micro.core.function.dto.MessageChain
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface CommandService {

    fun cmd(): Cmd

    fun configHint(): ConfigHint? = null

    fun describe(): Uni<String>

    fun invoke(args: MutableList<String>, config: Map<String, *>): Uni<MessageChain>

}