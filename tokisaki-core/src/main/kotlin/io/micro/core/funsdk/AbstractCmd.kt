package io.micro.core.funsdk

/**
 *@author Augenstern
 *@since 2023/10/8
 */
abstract class AbstractCmd : Command {
    override fun args() = ArgsMergeStrategy.Default
}