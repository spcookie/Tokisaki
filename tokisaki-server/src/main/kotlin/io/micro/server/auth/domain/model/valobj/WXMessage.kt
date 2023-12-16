package io.micro.server.auth.domain.model.valobj

data class WXMessage(
    /**
     * 开发者微信号
     */
    var toUserName: String,

    /**
     * 发送方账号（一个OpenID）
     */
    var fromUserName: String,

    /**
     * 消息创建时间 （整型）
     */
    var createTime: String,

    /**
     * 消息类型，文本为text
     */
    var msgType: String,

    /**
     * 文本消息内容
     */
    var content: String,

    /**
     * 消息id，64位整型
     */
    var msgId: String,

    /**
     * 消息的数据ID（消息如果来自文章时才有）
     */
    var msgDataId: String?,

    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    var idx: String?
)