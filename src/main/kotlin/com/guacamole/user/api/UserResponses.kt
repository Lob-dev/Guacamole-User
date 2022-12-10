package com.guacamole.user.api

data class AccessTokenResponse(
    val accessToken: String,
    val accessTokenExpireAt: Int,
)