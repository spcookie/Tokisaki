package io.micro.core.auth

import io.smallrye.jwt.build.Jwt
import org.eclipse.microprofile.jwt.Claims
import java.time.Duration


/**
 *@author Augenstern
 *@since 2023/11/25
 */
object GenerateJwtToken {

    private val expires = Duration.ofDays(7)

    private val issuer = "Tokisaki"

    /**
     * 生成JWT令牌
     */
    fun generate(name: String, openId: String, authorities: Set<String>): String {
        return Jwt.issuer(issuer)
            .upn(openId)
            .groups(authorities)
            .claim(Claims.nickname.name, name)
            .expiresIn(expires)
            .sign()
    }

}