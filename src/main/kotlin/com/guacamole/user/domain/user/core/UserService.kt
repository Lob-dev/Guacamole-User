package com.guacamole.user.domain.user.core

import com.guacamole.user.application.UserRegistrationCommand
import com.guacamole.user.common.utils.HashGenerator.sha256
import com.guacamole.user.domain.user.core.model.UserType
import com.guacamole.user.domain.user.privacy.UserPrivacy
import com.guacamole.user.domain.user.privacy.UserPrivacyRepository
import com.guacamole.user.domain.user.terms.TermsRepository
import com.guacamole.user.domain.user.terms.UserTermsAgree
import com.guacamole.user.domain.user.terms.UserTermsAgreeRepository
import com.guacamole.user.external.BusinessRegistrationApi
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneId

@Service
class UserService(
    private val userRepository: UserRepository,
    private val termsRepository: TermsRepository,
    private val userPrivacyRepository: UserPrivacyRepository,
    private val userTermsAgreeRepository: UserTermsAgreeRepository,
    private val businessRegistrationApi: BusinessRegistrationApi,
) {

    @Transactional
    fun registration(command: UserRegistrationCommand): User {
        when (command.userType) {
            UserType.CUSTOMER -> {}
            UserType.SELLER -> {
                if (businessRegistrationApi.check(command.registrationNumber!!).not()) {
                    throw RuntimeException("Not Allowed Registration number")
                }
            }
        }
        val newUser = userRepository.save(command.toUser())
        userPrivacyRepository.save(UserPrivacy(newUser.id!!, command.privacy.ci))

        val acceptAt = LocalDate.now(ZoneId.of("Asia/Seoul"))
        val userTermsAgrees =
            termsRepository.findAllByTermsTypeIn(command.terms.map { it.termsType }.toList())
                .map { UserTermsAgree.of(newUser.id!!, it.id!!, acceptAt) }
                .toList()
        userTermsAgreeRepository.saveAll(userTermsAgrees)
        return newUser
    }

    @Transactional(readOnly = true)
    fun isDuplicateContactNumber(contactNumber: String): Boolean =
        userRepository.existsByContactNumber(contactNumber)

    @Transactional
    fun approveAuthorize(userId: Long) {
        val targetUser = findById(userId)
        targetUser.authorize()
    }

    @Transactional(readOnly = true)
    fun findById(userId: Long): User {
        return userRepository.findByIdOrNull(userId)
            ?: throw RuntimeException("Not Found User")
    }

    @Transactional(readOnly = true)
    fun isAllowedUserDetails(email: String, password: String): Boolean {
        val targetUser = userRepository.findByEmail(email)
        return targetUser.isMatchedPassword(password.sha256())
    }
}
