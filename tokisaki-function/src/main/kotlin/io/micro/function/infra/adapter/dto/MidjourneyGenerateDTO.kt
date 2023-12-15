package io.micro.function.infra.adapter.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@author Augenstern
 *@since 2023/10/19
 */
@Serializable
data class MidjourneyGenerateDTO(
    @SerialName("image_url") val imageUrl: String,
    @SerialName("image_width") val imageWidth: Int,
    @SerialName("image_height") val imageHeight: Int,
    @SerialName("image_id") val imageId: Long,
    @SerialName("raw_image_url") val rawImageUrl: String,
    @SerialName("raw_image_width") val rawImageWidth: Int,
    @SerialName("raw_image_height") val rawImageHeight: Int,
    @SerialName("progress") val progress: Int,
    @SerialName("actions") val actions: List<String>,
    @SerialName("task_id") val taskId: String,
    @SerialName("success") val success: Boolean
)
