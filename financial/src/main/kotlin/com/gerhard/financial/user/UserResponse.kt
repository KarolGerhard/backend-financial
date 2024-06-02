package com.gerhard.financial.user

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    constructor(u: UserEntity): this(
        id=u.id!!,
        name=u.name,
        email=u.email
    )
}