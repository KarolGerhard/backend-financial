package com.gerhard.financial.user

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

