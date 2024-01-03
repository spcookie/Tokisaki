package io.micro.api.robot.dto

import jakarta.validation.Valid
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "机器人信息")
@Serializable
class QueryRobotDTO {

    @Schema(title = "用户ID")
    var userId: Long? = null

    @Schema(title = "机器人ID")
    var id: Long? = null

    @Schema(title = "名称")
    var name: String? = null

    @Schema(title = "帐号")
    var account: String? = null

    @Schema(title = "密码")
    var password: String? = null

    @Schema(title = "类型", anyOf = [])
    var type: Int? = null

    @Schema(title = "状态")
    var state: Int? = null

    @Schema(title = "备注")
    var remark: String? = null

    @Schema(title = "功能列表")
    val functions: MutableList<@Valid QueryFeatureFunctionDTO> = mutableListOf()

}