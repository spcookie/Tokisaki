package io.micro.api.user.dto

import io.micro.core.valid.ValidGroup
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "操作权限")
@Serializable
class OperateAuthorityDTO {

    @NotNull(groups = [ValidGroup.Update::class])
    @Schema(title = "权限ID")
    var id: Long? = null

    @NotNull(groups = [ValidGroup.Create::class])
    @Schema(title = "权限值")
    var value: String? = null

    @Schema(title = "备注")
    var remark: String? = null

    @Schema(title = "是否启用")
    var enabled: Boolean? = null

}