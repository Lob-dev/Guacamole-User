package com.guacamole.user.common.utils

import java.security.MessageDigest

object HashGenerator {
    private val cachedMessageDigest: MutableMap<String, MessageDigest> = mutableMapOf()

    fun String.sha256(): String = hashString(this, "SHA-256")
    fun String.hashFunction(algorithm: String): String = hashString(this, algorithm)

    private fun hashString(input: String, algorithm: String): String {
        val messageDigest = if (cachedMessageDigest.containsKey(algorithm)) {
            cachedMessageDigest[algorithm]
        } else {
            MessageDigest.getInstance(algorithm)
        }

        return messageDigest!!
            .digest(input.toByteArray())
            .fold("") { accessor, short -> "$accessor${"%02x".format(short)}" }
    }
}