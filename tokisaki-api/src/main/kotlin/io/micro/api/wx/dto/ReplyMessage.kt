package io.micro.api.wx.dto

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient

@XmlRootElement(name = "xml")
class ReplyMessage {

    /**
     * 开发者微信号
     */
    @set:XmlTransient
    @XmlElement(name = "ToUserName")
    var toUserName: String? = null

    /**
     * 发送方账号（一个OpenID）
     */
    @set:XmlTransient
    @XmlElement(name = "FromUserName")
    var fromUserName: String? = null

    /**
     * 消息创建时间 （整型）
     */
    @set:XmlTransient
    @XmlElement(name = "CreateTime")
    var createTime: String? = null

    /**
     * 消息类型，文本为text
     */
    @set:XmlTransient
    @XmlElement(name = "MsgType")
    var msgType: String? = null

    /**
     * 文本消息内容
     */
    @set:XmlTransient
    @XmlElement(name = "Content")
    var content: String? = null

}