package com.guacamole.user.application

import com.guacamole.user.common.utils.HashGenerator.sha256
import com.guacamole.user.domain.user.core.User
import com.guacamole.user.domain.user.core.model.UserStatus
import com.guacamole.user.domain.user.core.model.UserType
import com.guacamole.user.domain.user.privacy.model.PrivacyDetail
import com.guacamole.user.domain.user.terms.model.Terms
import java.time.LocalDate
import java.time.ZoneId

data class UserRegistrationCommand(
    val username: String,
    val nickname: String,
    val email: String,
    val password: String,
    val contactNumber: String,
    val birthday: LocalDate,
    val userType: UserType,
    val registrationNumber: String?,
    val privacy: PrivacyDetail,
    val terms: List<Terms>,
) {

    fun toUser(): User = User(
        username = username,
        nickname = nickname,
        email = email,
        password = password.sha256(),
        passwordExpireAt = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(ONE_MONTH),
        birthday = birthday,
        contactNumber = contactNumber,
        userStatus = UserStatus.ACTIVE,
    )

    companion object {
        const val ONE_MONTH: Long = 30
    }
}