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
    val password: String,
    val passwordExpireAt: LocalDate,
    val birthday: LocalDate,
    val contactNumber: String,
    @Enumerated(EnumType.STRING)
    var userStatus: UserStatus,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
) {

    fun authorize() {
        this.userStatus = UserStatus.ACTIVE
    }

    fun isMatchedPassword(inputPassword: String): Boolean = password == inputPassword
}
