package com.guacamole.user.application

import com.guacamole.user.common.TransactionHandler
import com.guacamole.user.domain.authorize.AuthorizeCodeManager
import com.guacamole.user.domain.mail.MailService
import com.guacamole.user.domain.user.core.UserService
import com.guacamole.user.domain.user.core.model.UserType
import org.springframework.stereotype.Service

@Service
class UserFacadeService(
    private val userService: UserService,
    private val authorizeCodeManager: AuthorizeCodeManager,
    private val mailService: MailService,
    private val transactionHandler: TransactionHandler,
) {

    fun registration(command: UserRegistrationCommand) {
        if (userService.isDuplicateContactNumber(command.contactNumber)) {
            throw RuntimeException("is already registration contact number")
        }

        transactionHandler.runInTransaction {
            val newUser = userService.registration(command)
            val authorizeCode = authorizeCodeManager.publishAuthorizeCode(newUser.id!!)
            mailService.sendAuthorizeEmail(command.email, newUser.id!!, authorizeCode)
        }
    }

    fun approveAuthorize(
        userId: Long,
        authCode: String
    ): Boolean {
        if (authorizeCodeManager.isAllowedAuthorizeCode(userId, authCode).not()) {
            throw RuntimeException("Not Allowed Auth Code")
        }

        userService.approveAuthorize(userId)
        return true
    }

    fun isAllowedUserDetails(email: String, password: String, userType: UserType): Boolean =
        userService.isAllowedUserDetails(email, password)
}