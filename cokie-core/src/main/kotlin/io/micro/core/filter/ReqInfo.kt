package io.micro.core.filter

import jakarta.enterprise.context.RequestScoped

/**
 *@author Augenstern
 *@since 2023/11/25
 */
@RequestScoped
class ReqInfo {
    var driveId: String? = null
}