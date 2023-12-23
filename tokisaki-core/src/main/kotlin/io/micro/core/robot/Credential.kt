package io.micro.core.robot

/**
 *@author Augenstern
 *@since 2023/11/21
 */
data class Credential(
    val id: Long,
    val account: String,
    val password: String = ""
)