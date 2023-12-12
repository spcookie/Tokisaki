package io.micro.core.login

import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.jwt.Claims


/**
 *@author Augenstern
 *@since 2023/11/25
 */
object GenerateToken {
    /**
     * 生成JWT令牌
     */
    fun generate(name: String, openId: String, authorities: Set<String>): String {
        return Jwt.issuer("https://example.com/issuer")
            .upn(name)
            .groups(authorities)
            .claim(Claims.orig.name, openId)
            .sign()
    }
}