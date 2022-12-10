package com.guacamole.user.domain.authorize

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.guacamole.user.config.properties.AccessTokenProperty
import com.guacamole.user.domain.authorize.model.AccessToken
import com.guacamole.user.domain.user.core.User
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class AccessTokenManager(
    accessTokenProperty: AccessTokenProperty,
) {
    private val issuer: String = accessTokenProperty.issuer
    private val expireAtBySeconds = accessTokenProperty.expireAtBySeconds
    private var algorithm: Algorithm =
        Algorithm.HMAC256(accessTokenProperty.secret.toByteArray(StandardCharsets.UTF_8))

    fun generate(user: User): AccessToken {
        val accessExpireTime = LocalDateTime.now()
            .plusSeconds(expireAtBySeconds)
            .atZone(ZoneId.of("Asia/Seoul")).toInstant()

        val accessToken = JWT.create()
            .withIssuer(issuer)
            .withClaim("id", user.id!!)
            .withClaim("birthDay", user.birthday.toString())
            .withExpiresAt(accessExpireTime)
            .sign(algorithm)
        return AccessToken(accessToken, expireAtBySeconds)
    }
}