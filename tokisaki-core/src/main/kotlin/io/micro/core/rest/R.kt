package io.micro.core.rest

import kotlinx.serialization.Serializable
import org.eclipse.microprofile.openapi.annotations.media.Schema

/**
 *@author Augenstern
 *@since 2023/11/24
 */
@Serializable
@Schema(title = "通用结果")
class R<T> {

    @Schema(title = "状态码")
    val code: Int = CommonCode.OK.code

    @Schema(title = "结果信息")
    var message: String = ""

    @Schema(title = "结果数据")
    var data: T? = null

    companion object {
        @JvmStatic
        @JvmOverloads
        fun <E> newInstance(data: E? = null, msg: String = CommonCode.OK.msg): R<E> {
            return R<E>().apply {
                this.message = msg
                this.data = data
            }
        }
    }
}