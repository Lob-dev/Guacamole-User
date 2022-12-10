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
        userFacadeService.authorize(userId, authCode)

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: UserLoginRequest): AccessTokenResponse =
        userFacadeService.login(request.email, request.password, request.userType)
}
