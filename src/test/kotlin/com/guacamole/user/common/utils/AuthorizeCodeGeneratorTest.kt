package com.guacamole.user.common.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AuthorizeCodeGeneratorTest {

    @Test
    fun `정상적으로 인증 코드가 생성되는지 확인한다`() {
        val firstGenerateAuthorizeCode = AuthorizeCodeGenerator.generateAuthorizeCode()
        val secondGenerateAuthorizeCode = AuthorizeCodeGenerator.generateAuthorizeCode()

        assertAll(
            { assertNotNull(firstGenerateAuthorizeCode) },
            { assertNotSame(firstGenerateAuthorizeCode, secondGenerateAuthorizeCode) },
            { assertEquals(10, firstGenerateAuthorizeCode.length) },
        )
    }
}