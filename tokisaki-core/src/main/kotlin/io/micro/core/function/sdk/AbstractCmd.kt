package io.micro.core.function.sdk

/**
 *@author Augenstern
 *@since 2023/10/8
 */
abstract class AbstractCmd : Command {
    override fun args() = ArgsMergeStrategy.Default
}