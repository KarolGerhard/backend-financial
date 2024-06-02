package com.gerhard.financial.user

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @NotNull
    var name: String,
    @Column(unique = true, nullable = false)
    var email: String,
    @NotNull
    var password: String
)
