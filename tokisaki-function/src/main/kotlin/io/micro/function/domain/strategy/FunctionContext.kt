package io.micro.function.domain.strategy

import io.micro.core.fundto.MessageChain
import io.micro.core.funsdk.Cmd
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface FunctionContext {

    fun call(cmd: Cmd, args: MutableList<String>): Uni<MessageChain>

    fun menu(): Uni<MessageChain>

    fun downloadImage(cmd: Cmd): Uni<Void>

    fun removeImage(cmd: Cmd): Uni<Void>
}