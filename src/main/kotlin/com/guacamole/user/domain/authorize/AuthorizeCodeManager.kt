package com.guacamole.user.domain.authorize

import com.guacamole.user.common.utils.AuthorizeCodeGenerator
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AuthorizeCodeManager {
    private val cachedAuthorizeCodes = ExpiringMap.builder()
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expiration(THREE_MINUTE, TimeUnit.MINUTES)
        .build<Long, String>()

    fun publish(userId: Long): String =
        AuthorizeCodeGenerator.generateAuthorizeCode()
            .apply { cachedAuthorizeCodes[userId] = this }

    fun isAllowedAuthorizeCode(userId: Long, authCode: String): Boolean {
        if (cachedAuthorizeCodes.containsKey(userId).not()) {
            throw RuntimeException("is expired authorize code")
        }

        if (cachedAuthorizeCodes[userId] == authCode) {
            return true
        }
        throw RuntimeException("not allowed authorize code")
    }

    companion object {
        const val THREE_MINUTE: Long = 3
    }
}
