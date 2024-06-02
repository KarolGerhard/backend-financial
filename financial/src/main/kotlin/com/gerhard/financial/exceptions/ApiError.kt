package com.gerhard.financial.exceptions

import java.time.LocalDateTime

data class ApiError<T>(
    val timestamp: LocalDateTime?,
    val code: Int?,
    val status: String?,
    val errors: T
)