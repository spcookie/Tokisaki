package io.micro.api.wx.dto

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "微信回复消息")
@XmlRootElement(name = "xml")
class ReplyMessageDTO {

    @Schema(title = "开发者微信号")
    @set:XmlTransient
    @XmlElement(name = "ToUserName")
    var toUserName: String? = null

    @Schema(title = "发送方账号（一个OpenID）")
    @set:XmlTransient
    @XmlElement(name = "FromUserName")
    var fromUserName: String? = null

    @Schema(title = "消息创建时间 （整型）")
    @set:XmlTransient
    @XmlElement(name = "CreateTime")
    var createTime: String? = null

    @Schema(title = "消息类型，文本为text")
    @set:XmlTransient
    @XmlElement(name = "MsgType")
    var msgType: String? = null

    @Schema(title = "文本消息内容")
    @set:XmlTransient
    @XmlElement(name = "Content")
    var content: String? = null

}