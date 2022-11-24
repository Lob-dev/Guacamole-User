package com.guacamole.user.external

import org.springframework.stereotype.Service

/**
 * 현재는 Mock API
 * https://www.data.go.kr/data/15081808/openapi.do
 */
@Service
class BusinessRegistrationApi {

    fun check(registrationNumber: String): Boolean {
        return true
    }
}
