package io.micro.core.funsdk

import io.smallrye.mutiny.Uni

/**
 *@author Augenstern
 *@since 2023/10/8
 */
interface Command {
    fun identify(): Cmd
    fun describe(call: Long): Uni<String>
    fun args(): ArgsMergeStrategy
}