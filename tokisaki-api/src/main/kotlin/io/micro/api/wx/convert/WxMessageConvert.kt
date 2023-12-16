package io.micro.api.wx.convert

import io.micro.api.wx.dto.WxMessageDTO
import io.micro.server.auth.domain.model.valobj.WXMessage
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(componentModel = ComponentModel.CDI)
interface WxMessageConvert {
    fun wxMessageDTO2WxMessage(message: WxMessageDTO): WXMessage
}