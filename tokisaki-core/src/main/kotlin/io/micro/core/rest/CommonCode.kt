package io.micro.core.rest

/**
 *@author Augenstern
 *@since 2023/11/24
 */
enum class CommonCode(val code: Int, val msg: String) {
    IGNORE(1000, "忽略"),
    OK(1200, "操作成功"),
    FAIL(1590, "操作失败"),
    ERROR(1500, "系统错误"),
    NOT_FOUND(1404, "不存在资源"),
    ILLEGAL_OPERATION(1403, "非法操作"),
    ILLEGAL_STATE(1403, "非法状态"),
    DUPLICATE(1591, "重复操作")
}