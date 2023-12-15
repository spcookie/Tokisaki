package io.micro.function.infra.adapter.dto

import kotlinx.serialization.Serializable


/**
 *@author Augenstern
 *@date 2023/6/4
 */
@Serializable
data class RandomURLDTO(var code: Int = 0, var url: String = "")