package com.guacamole.user.domain.user.terms

import com.guacamole.user.domain.user.terms.model.TermsType
import org.springframework.data.jpa.repository.JpaRepository

interface TermsRepository : JpaRepository<Terms, Long> {

    fun findAllByTermsTypeIn(termsType: List<TermsType>): List<Terms>
}