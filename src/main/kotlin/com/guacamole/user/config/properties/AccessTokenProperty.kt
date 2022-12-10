package com.guacamole.user.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "access-token")
data class AccessTokenProperty(
    val secret: String,
    val issuer: String,
    val expireAtBySeconds: Long,
)