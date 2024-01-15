package io.micro.function.infra.converter

import io.micro.function.domain.image.model.Image
import io.micro.function.domain.image.model.entity.AnimeCartoon
import io.micro.function.domain.image.model.entity.Girl
import io.micro.function.infra.po.ImagePO
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

/**
 *@author Augenstern
 *@since 2023/10/9
 */
@Mapper(componentModel = MappingConstants.ComponentModel.CDI, uses = [ImageMapper::class])
interface ImageConverter {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun toImagePO(image: Image): ImagePO

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun toGirlDO(image: ImagePO): Girl

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    fun toAnimeDO(image: ImagePO): AnimeCartoon

}