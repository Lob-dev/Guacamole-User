package com.guacamole.user.domain.user.core

import com.guacamole.user.domain.user.core.model.UserStatus
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    val username: String,
    val nickname: String,
    val email: String,
    var isAuthorized: Boolean = false,
    val password: String,
    val passwordExpireAt: LocalDate,
    val birthday: LocalDate,
    val contactNumber: String,
    @Enumerated(EnumType.STRING)
    val userStatus: UserStatus,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    fun isMatchedPassword(inputPassword: String): Boolean = password == inputPassword
}