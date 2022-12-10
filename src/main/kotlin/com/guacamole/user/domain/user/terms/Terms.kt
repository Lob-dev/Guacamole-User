package com.guacamole.user.domain.user.terms

import com.guacamole.user.domain.user.terms.model.TermsType
import javax.persistence.*

@Entity
@Table(name = "terms")
class Terms(
    @Enumerated(EnumType.STRING)
    val termsType: TermsType,
    val isRequired: Boolean,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
}
