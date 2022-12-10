package com.guacamole.user.api

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserRegistrationRequestTest {

    @ValueSource(strings = ["1aA@testbb", "abcD123$", "2234AB@c", "ppoeE2#$"])
    @ParameterizedTest
    fun `비밀번호가 소문자, 대문자, 숫자, 특수 문자를 1개 이상 포함하고 8자리가 넘는다면 True를 리턴한다`(password: String) {
        val fixture = UserRegistrationRequest.fixture(password)
        val isAllowedPassword = fixture.isAllowedPassword()
        assertTrue(isAllowedPassword)
    }

    @ValueSource(strings = ["1aA@", "1aA@2z$", "1aA@423", "!@!@1Aa"])
    @ParameterizedTest
    fun `비밀번호가 소문자, 대문자, 숫자, 특수 문자를 1개 이상 포함하더라도 8자리가 되지 않는다면 False를 리턴한다`(password: String) {
        val fixture = UserRegistrationRequest.fixture(password)
        val isAllowedPassword = fixture.isAllowedPassword()
        assertFalse(isAllowedPassword)
    }

    @ValueSource(strings = ["abCd1234", "abcd123$", "ABCD!@#1", "!@#$!@1a"])
    @ParameterizedTest
    fun `비밀번호가 8자리를 넘더라도 소문자, 대소문자, 숫자, 특수 문자 중 하나라도 누락한다면 False를 리턴한다`(password: String) {
        val fixture = UserRegistrationRequest.fixture(password)
        val isAllowedPassword = fixture.isAllowedPassword()
        assertFalse(isAllowedPassword)
    }
}