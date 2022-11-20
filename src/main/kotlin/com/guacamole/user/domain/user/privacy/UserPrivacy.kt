package com.guacamole.user.domain.user.privacy

import com.guacamole.user.common.entity.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "user_privacy")
class UserPrivacy(
    val userId: Long,
    val ci: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseTimeEntity() {
}