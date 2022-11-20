package com.guacamole.user.domain.user.privacy.model

import javax.validation.constraints.NotNull

class PrivacyDetail(
    @get:NotNull
    val ci: String,
) {
}