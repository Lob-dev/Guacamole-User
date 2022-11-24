package com.guacamole.user.domain.user.privacy

import org.springframework.data.jpa.repository.JpaRepository

interface UserPrivacyRepository : JpaRepository<UserPrivacy, Long> {
}
