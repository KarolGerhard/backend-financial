package com.gerhard.financial.user

import com.gerhard.financial.category.CategoryDTORequest
import com.gerhard.financial.exceptions.CategoryHasRegisteredException
import com.gerhard.financial.security.Jwt
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val jwt: Jwt){

    fun save(user: UserRequest): UserResponse {
        checkIfExists(user)

        val save = userRepository.save(user.toUser())

        return UserResponse(save)
    }

    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email).firstOrNull()

        if (user == null) {
            log.warn("User {} not found!", email)
            return null
        }
        if (password != user.password) {
            log.warn("Invalid password!")
            return null
        }
        log.info("User logged in: id={}, name={}", user.id, user.name)
        return LoginResponse(
            token = jwt.createToken(user),
            UserResponse(user)
        )
    }

    private fun checkIfExists(user: UserRequest) {
        val hasUser = userRepository.findByEmail(user.email!!).isNotEmpty()
        if (hasUser) throw CategoryHasRegisteredException(
            message = "Já existe um usuário registrado com o e-mail informado."
        )
    }

    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }
}