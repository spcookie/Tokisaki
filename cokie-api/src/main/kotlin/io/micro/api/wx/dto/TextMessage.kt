package io.micro.api.wx.dto

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlTransient

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@XmlRootElement
class TextMessage {

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

    /**
     * 消息id，64位整型
     */
    @set:XmlTransient
    @XmlElement(name = "MsgId")
    var msgId: String? = null

    /**
     * 消息的数据ID（消息如果来自文章时才有）
     */
    @set:XmlTransient
    @XmlElement(name = "MsgDataId")
    var msgDataId: String? = null

    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    @set:XmlTransient
    @XmlElement(name = "Idx")
    var idx: String? = null
}
