package com.gerhard.financial.security

import com.gerhard.financial.user.UserEntity

data class UserToken(
    val id: Long,
    val name: String,
    val email: String,
) {
    constructor(): this(0, "", "")
    constructor(user: UserEntity): this(
        id = user.id!!,
        name = user.name,
        email = user.email,
    )

}
