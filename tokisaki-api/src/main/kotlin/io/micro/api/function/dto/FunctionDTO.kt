package io.micro.api.function.dto

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "功能信息")
@Serializable
class FunctionDTO {

    @Schema(name = "功能信息ID")
    var id: Long? = null

    @Schema(name = "功能名称")
    var name: String? = null

    @Schema(name = "功能标识")
    var code: String? = null

    @Schema(name = "配置信息")
    var config: String? = null

}