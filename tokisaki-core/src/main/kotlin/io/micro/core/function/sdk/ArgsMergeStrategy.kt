package io.micro.core.function.sdk

/**
 *@author Augenstern
 *@since 2023/10/8
 */
interface ArgsMergeStrategy {

    fun strategy(originalArgs: List<String>): MutableList<String>

    companion object {
        val Default = object : ArgsMergeStrategy {
            override fun strategy(originalArgs: List<String>): MutableList<String> {
                return originalArgs.toMutableList()
            }
        }
        val Connect = object : ArgsMergeStrategy {
            override fun strategy(originalArgs: List<String>): MutableList<String> {
                return if (originalArgs.isEmpty()) mutableListOf() else mutableListOf(originalArgs.reduce { acc, s -> acc + s })
            }
        }

        val Merge = object : ArgsMergeStrategy {
            override fun strategy(originalArgs: List<String>): MutableList<String> {
                return if (originalArgs.isEmpty()) mutableListOf() else mutableListOf(originalArgs.joinToString(" "))
            }
        }
    }
}