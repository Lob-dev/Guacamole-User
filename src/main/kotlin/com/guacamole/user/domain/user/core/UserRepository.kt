package com.guacamole.user.domain.user.core

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun existsByContactNumber(contactNumber: String): Boolean
    fun findByEmail(email: String): User
}