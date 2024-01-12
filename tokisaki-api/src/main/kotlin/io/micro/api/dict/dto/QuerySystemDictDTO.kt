package io.micro.api.dict.dto

import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

@Schema(title = "系统数据字典")
@Serializable
class QuerySystemDictDTO {

    @Schema(title = "系统字典ID")
    var id: Long? = null

    @Schema(title = "字典键")
    @NotNull
    var key: String? = null

    @Schema(title = "字典值")
    @NotNull
    var value: String? = null

    @Schema(title = "值类型")
    @NotNull
    var type: Int? = null

}