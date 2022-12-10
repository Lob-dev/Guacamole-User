package com.guacamole.user.application

import com.fasterxml.jackson.databind.ObjectMapper
import com.guacamole.user.api.AccessTokenResponse
import com.guacamole.user.common.TransactionHandler
import com.guacamole.user.domain.authorize.AccessTokenManager
import com.guacamole.user.domain.authorize.AuthorizeCodeManager
import com.guacamole.user.domain.mail.outbox.model.AuthorizationMailProperty
import com.guacamole.user.domain.mail.outbox.MailOutbox
import com.guacamole.user.domain.mail.outbox.MailOutboxRelay
import com.guacamole.user.domain.mail.outbox.model.MailType
import com.guacamole.user.domain.user.core.UserService
import com.guacamole.user.domain.user.core.model.UserType
import org.springframework.stereotype.Service

@Service
class UserFacadeService(
    private val userService: UserService,
    private val authorizeCodeManager: AuthorizeCodeManager,
    private val accessTokenManager: AccessTokenManager,
    private val mailOutboxRelay: MailOutboxRelay,
    private val transactionHandler: TransactionHandler,
    private val objectMapper: ObjectMapper,
) {

    fun registration(command: UserRegistrationCommand) {
        if (userService.isDuplicateContactNumber(command.contactNumber)) {
            throw RuntimeException("is already registration contact number")
        }

        transactionHandler.runInTransaction {
            val newUser = userService.registration(command)
            val userId = newUser.id!!
            val authorizeCode = authorizeCodeManager.publish(userId)

            val property = objectMapper.writeValueAsString(
                AuthorizationMailProperty(userId, command.email, authorizeCode)
            )
            mailOutboxRelay.publish(MailOutbox.create(property, MailType.AUTHORIZATION))
        }
    }

    fun authorize(
        userId: Long,
        authCode: String
    ): Boolean {
        if (authorizeCodeManager.isAllowedAuthorizeCode(userId, authCode).not()) {
            throw RuntimeException("Not Allowed Auth Code")
        }

        userService.authorize(userId)
        return true
    }

    fun login(email: String, password: String, userType: UserType): AccessTokenResponse {
        val user = userService.login(email, password)
        val accessToken = accessTokenManager.generate(user)
        return AccessTokenResponse(accessToken.value, accessToken.expireAt)
    }
}
