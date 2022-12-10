package com.guacamole.user.api

import com.guacamole.user.application.UserRegistrationCommand
import com.guacamole.user.domain.user.core.model.UserType
import com.guacamole.user.domain.user.privacy.model.PrivacyDetail
import com.guacamole.user.domain.user.terms.model.Terms
import com.guacamole.user.domain.user.terms.model.TermsType
import org.hibernate.validator.constraints.Length
import java.time.LocalDate
import javax.validation.Valid
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

object PasswordPolicy {
    private val PASSWORD_POLICY_REGEX: Regex =
        Regex("((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,})")

    fun isMatchedPassword(password: String): Boolean =
        PASSWORD_POLICY_REGEX.containsMatchIn(password)
}

data class UserRegistrationRequest(
    @get:NotBlank
    val username: String,
    @get:NotBlank
    val nickname: String,
    @get:Email
    val email: String,
    @get:Length
    @get:NotBlank
    val password: String,
    @get:NotBlank
    val contactNumber: String,
    @get:NotNull
    val birthday: LocalDate,
    @get:NotNull
    val userType: UserType,
    val registrationNumber: String?,
    @Valid
    @get:NotNull
    val privacy: PrivacyDetail,
    @Valid
    @get:NotNull
    val terms: List<Terms>
) {

    fun toCommand(): UserRegistrationCommand = UserRegistrationCommand(
        username = username,
        nickname = nickname,
        email = email,
        password = password,
        contactNumber = contactNumber,
        birthday = birthday,
        userType = userType,
        registrationNumber = registrationNumber,
        privacy = privacy,
        terms = terms,
    )

    @AssertTrue
    fun isAllowedPassword(): Boolean {
        return PasswordPolicy.isMatchedPassword(password)
    }

    @AssertTrue
    fun isSellerRegistration(): Boolean {
        if (userType == UserType.CUSTOMER) {
            return true
        }

        if (registrationNumber != null) {
            return true
        }

        terms.find { it.termsType == TermsType.CORPORATION && it.isAgree.not() }
            ?: throw RuntimeException("Seller must agreement terms of corporation")
        return false
    }

    companion object {
        fun fixture(password: String): UserRegistrationRequest = UserRegistrationRequest(
            username = "lob",
            nickname = "lob",
            email = "lob@test.com",
            password = password,
            contactNumber = "010-1111-1111",
            birthday = LocalDate.now().minusYears(20),
            userType = UserType.CUSTOMER,
            registrationNumber = "000-00-00000",
            privacy = PrivacyDetail("ci"),
            terms = listOf(Terms(TermsType.SERVICE, true))
        )
    }
}


data class UserLoginRequest(
    @get:Email
    val email: String,
    @get:Length
    @get:NotBlank
    val password: String,
    @get:NotNull
    val userType: UserType,
) {

    @AssertTrue
    fun isAllowedPassword(): Boolean {
        return PasswordPolicy.isMatchedPassword(password)
    }
}
