package io.micro.core.robot

/**
 *@author Augenstern
 *@since 2023/11/21
 */
data class Credential(
    val identify: String,
    val evidence: String = ""
)