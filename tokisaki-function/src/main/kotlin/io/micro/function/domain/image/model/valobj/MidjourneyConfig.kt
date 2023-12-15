package io.micro.function.domain.image.model.valobj

import io.smallrye.config.ConfigMapping

/**
 *@author Augenstern
 *@since 2023/10/21
 */
@ConfigMapping(prefix = "midjourney")
interface MidjourneyConfig {
    fun apiKey(): String
}