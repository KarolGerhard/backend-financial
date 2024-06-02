package com.gerhard.financial

import com.gerhard.financial.category.CategoryEntity
import com.gerhard.financial.category.CategoryRepository
import com.gerhard.financial.transaction.TransactionEntity
import com.gerhard.financial.transaction.TransactionRepository
import com.gerhard.financial.transaction.TransactionType
import com.gerhard.financial.user.UserEntity
import com.gerhard.financial.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@Profile("!test")
class Bootstrapper(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) :
    ApplicationListener<ContextRefreshedEvent> {

    @Value("\${spring.profiles.active}")
    private val activeProfile: String? = null

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (activeProfile != null && activeProfile != "test") {
            seedCategories()
            seedUser()
            seedTransactions()
        }

    }

    private fun seedCategories() {
        val hasCategories = categoryRepository.count() > 0;

        if (!hasCategories) {
            categoryRepository.saveAll(
                listOf(
                    CategoryEntity(id = 1, "Alimentação", createdAt = LocalDateTime.now()),
                    CategoryEntity(id = 2, "Salário", createdAt = LocalDateTime.now()),
                    CategoryEntity(id = 3, "Transporte", createdAt = LocalDateTime.now())
                )
            )
        }
    }


    private fun seedUser() {
        val hasUser = userRepository.count() > 0;

        if (!hasUser) {
            userRepository.saveAll(
                listOf(
                    UserEntity(id = 1, name = "Hello World", email = "hello@world.com", password = "helloworld1"),
                    UserEntity(id = 2, name = "Test", email = "test@test.com", password = "test1")
                )
            )
        }
    }


    private fun seedTransactions() {
        val hasTransactions = transactionRepository.count() > 0;
        if (!hasTransactions) {
            transactionRepository.saveAll(
                listOf(
                    TransactionEntity(
                        id = null,
                        title = "Salário",
                        description = "O pagode ta na conta",
                        user = UserEntity(
                            id = 1,
                            name = "Hello World",
                            email = "hello@world.com",
                            password = "helloworld1"
                        ),
                        category = categoryRepository.findByIdOrNull(2) ?: CategoryEntity(
                            id = 2,
                            "Salário",
                            createdAt = LocalDateTime.now()
                        ),
                        value = 2500.0,
                        type = TransactionType.RECIVE,
                        createdAt = LocalDateTime.now().minusDays(6)
                    ),
                    TransactionEntity(
                        id = null,
                        title = "Abastecimento",
                        description = "Abastecimento no posto shell",
                        user = UserEntity(
                            id = 1,
                            name = "Hello World",
                            email = "hello@world.com",
                            password = "helloworld1"
                        ),
                        category = categoryRepository.findByIdOrNull(3) ?: CategoryEntity(
                            id = 3,
                            "Transporte",
                            createdAt = LocalDateTime.now()
                        ),
                        value = 100.0,
                        type = TransactionType.EXPENSE,
                        createdAt = LocalDateTime.now().minusDays(3)
                    ),
                    TransactionEntity(
                        id = null,
                        title = "Almoço",
                        description = "Almoço no shopping",
                        user = UserEntity(
                            id = 1,
                            name = "Hello World",
                            email = "hello@world.com",
                            password = "helloworld1"
                        ),
                        category = categoryRepository.findByIdOrNull(1) ?: CategoryEntity(
                            id = 1,
                            "Alimentação",
                            createdAt = LocalDateTime.now()
                        ),
                        value = 80.0,
                        type = TransactionType.EXPENSE,
                        createdAt = LocalDateTime.now().minusDays(1)
                    ),
                    TransactionEntity(
                        id = null,
                        title = "Lanche da tarde",
                        description = "Café com pão de queijo",
                        user = UserEntity(
                            id = 1,
                            name = "Hello World",
                            email = "hello@world.com",
                            password = "helloworld1"
                        ),
                        category = categoryRepository.findByIdOrNull(1) ?: CategoryEntity(
                            id = 1,
                            "Alimentação",
                            createdAt = LocalDateTime.now()
                        ),
                        value = 47.5,
                        type = TransactionType.EXPENSE,
                        createdAt = LocalDateTime.now().minusHours(6)
                    ),
                )
            )
        }
    }
}