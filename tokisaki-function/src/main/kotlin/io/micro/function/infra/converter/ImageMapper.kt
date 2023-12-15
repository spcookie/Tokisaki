package io.net.spcokie.infra.converter

import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.Girl
import io.net.spcokie.infra.po.ImagePO
import io.net.spcokie.types.ImageCategory
import jakarta.inject.Singleton
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget

/**
 *@author Augenstern
 *@since 2023/10/12
 */
@Singleton
class ImageMapper {

    @AfterMapping
    fun imageToImagePO(image: Image, @MappingTarget po: ImagePO) {
        when (image) {
            is Girl -> po.apply { category = ImageCategory.GIRL }
            is AnimeCartoon -> po.apply { category = ImageCategory.ANIME }
            else -> throw IllegalArgumentException("不能映射: $image -> ImagePO")
        }
    }

}