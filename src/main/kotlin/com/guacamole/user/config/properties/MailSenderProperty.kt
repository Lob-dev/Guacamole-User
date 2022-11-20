package com.guacamole.user.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.mail")
data class MailSenderProperty(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
)