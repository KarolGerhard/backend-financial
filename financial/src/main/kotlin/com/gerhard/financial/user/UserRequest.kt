package com.gerhard.financial.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:NotBlank
    @field:Email
    val email: String?,
    @field:NotBlank
    val password: String?,
    val name: String?
) {
    fun toUser() = UserEntity(
        id = null,
        email = email!!,
        password = password!!,
        name = name!!
    )
}