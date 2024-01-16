package io.micro.api.robot.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(name = "绑定功能信息")
@Serializable
class QueryFeatureFunctionDTO {

    @Schema(title = "功能ID")
    @NotNull(groups = [ValidGroup.Create::class, ValidGroup.Update::class], message = "功能ID不能为空")
    var id: Long? = null

    @Schema(title = "引用功能ID")
    @NotNull(groups = [ValidGroup.Create::class, ValidGroup.Update::class], message = "引用功能ID不能为空")
    var refId: Long? = null

    @Schema(title = "名称")
    var name: String? = null

    @Schema(title = "配置")
    var config: String? = null

    @Schema(title = "备注")
    var remark: String? = null

    @Schema(title = "是否开启")
    var enabled: Boolean? = null

    @Schema(title = "功能别名")
    var cmdAlias: String? = null

    @Schema(title = "是否引用回复消息")
    var requireQuota: Boolean? = null

}