package com.guacamole.user.domain.user.terms

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "user_terms_agree")
class UserTermsAgree(
    var userId: Long,
    var termsId: Long,
    val acceptAt: LocalDate,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    companion object {
        fun of(userId: Long, termsId: Long, acceptAt: LocalDate): UserTermsAgree = UserTermsAgree(
            userId = userId,
            termsId = termsId,
            acceptAt = acceptAt,
        )
    }
}