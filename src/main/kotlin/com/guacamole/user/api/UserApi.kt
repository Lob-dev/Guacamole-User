package com.guacamole.user.api

import com.guacamole.user.application.UserFacadeService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserApi(
    private val userFacadeService: UserFacadeService,
) {

    @PostMapping
    fun registration(@RequestBody @Valid request: UserRegistrationRequest) {
        userFacadeService.registration(request.toCommand())
    }

    @GetMapping("/authorize")
    fun approveAuthorize(@RequestParam userId: Long, @RequestParam authCode: String): Boolean =
        userFacadeService.approveAuthorize(userId, authCode)

    @PostMapping("/login")
    fun approveLogin(@RequestBody @Valid request: UserApproveLoginRequest): Boolean =
        userFacadeService.isAllowedUserDetails(request.email, request.password, request.userType)
}
