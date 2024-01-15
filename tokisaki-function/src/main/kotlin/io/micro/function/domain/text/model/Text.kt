package io.micro.function.domain.text.model

import io.micro.core.entity.BaseDomainEntity

/**
 *@author Augenstern
 *@since 2023/10/20
 */
abstract class Text : BaseDomainEntity() {
    lateinit var content: String
}