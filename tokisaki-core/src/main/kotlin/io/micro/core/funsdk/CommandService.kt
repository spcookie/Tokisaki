package io.micro.core.funsdk

import io.micro.core.fundto.MessageChain
import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/10
 */
interface CommandService {
    fun cmd(): Cmd
    fun describe(): Uni<String>
    fun invoke(args: MutableList<String> = mutableListOf()): Uni<MessageChain>
}