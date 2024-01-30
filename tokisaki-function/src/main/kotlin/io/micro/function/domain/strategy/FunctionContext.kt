package io.micro.function.domain.strategy

import io.micro.core.function.ConfigHint
import io.micro.core.function.dto.MessageChain
import io.micro.core.function.sdk.Cmd
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface FunctionContext {

    fun call(cmd: Cmd, args: MutableList<String>, config: Map<String, *>): Uni<MessageChain>

    fun config(cmd: Cmd): ConfigHint?

    fun description(cmds: List<Cmd>): Uni<MessageChain>

    fun downloadImage(cmd: Cmd): Uni<Void>

    fun removeImage(cmd: Cmd): Uni<Void>
}