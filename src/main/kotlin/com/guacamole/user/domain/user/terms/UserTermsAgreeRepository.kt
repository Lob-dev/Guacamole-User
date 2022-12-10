package com.guacamole.user.domain.user.terms

import org.springframework.data.jpa.repository.JpaRepository

interface UserTermsAgreeRepository : JpaRepository<UserTermsAgree, Long> {
}
