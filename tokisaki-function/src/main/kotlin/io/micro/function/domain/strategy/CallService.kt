package io.micro.function.domain.strategy

import io.micro.core.fundto.Cmd
import io.micro.core.fundto.MessageChain
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface CallService {

    fun call(cmd: String, args: MutableList<String>): Uni<MessageChain>

    fun menu(): Uni<MessageChain>

    fun downloadImage(cmd: Cmd): Uni<Void>

    fun removeImage(cmd: Cmd): Uni<Void>
}