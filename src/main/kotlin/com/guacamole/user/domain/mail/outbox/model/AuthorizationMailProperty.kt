package com.guacamole.user.domain.mail.outbox.model

data class AuthorizationMailProperty(
    val userId: Long,
    val email: String,
    val authorizeCode: String,
) {
}
