package com.guacamole.user.common.utils

import kotlin.random.Random

object AuthorizeCodeGenerator {
    private val charPool: List<Char> = ('a'..'z') + ('0'..'9')
    private const val STRING_LENGTH = 10

    fun generateAuthorizeCode(): String =
        (1..STRING_LENGTH)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
}
