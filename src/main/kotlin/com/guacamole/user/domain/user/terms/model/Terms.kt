package com.guacamole.user.domain.user.terms.model

import javax.validation.constraints.NotNull

data class Terms(
    @get:NotNull
    val termsType: TermsType,
    val isAgree: Boolean = false,
)