package com.gerhard.financial.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.gerhard.financial.category.CategoryEntity
import com.gerhard.financial.category.CategoryRepository
import com.gerhard.financial.security.Jwt
import com.gerhard.financial.user.UserEntity
import com.gerhard.financial.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var transactionRepository: TransactionRepository

    @MockBean
    lateinit var categoryRepository: CategoryRepository

    @MockBean
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var jwt: Jwt


    private var token = "";


    @BeforeEach
    fun generateToken() {
        token = jwt.createToken(UserEntity(id = 1, name = "test", email = "test@test.com", password = ""))
    }


    @Test
    fun listTransactionShouldBeOk() {
        `when`(transactionRepository.findByUserId(anyLong())).thenReturn(listOf(transactionFixture))

        mockMvc.perform(
            get("/transaction")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].title").value(transactionFixture.title))
    }

    @Test
    fun createTransactionShouldBeOkWhenValid() {
        `when`(userRepository.findById(anyLong())).thenReturn(Optional.of(userFixture))
        `when`(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryFixture))
        `when`(transactionRepository.save(any())).thenReturn(transactionFixture)
        val createtransaction = CreateTransactionRequest(
            title = "Test",
            description = "Description test",
            value = 15.0,
            type = TransactionType.EXPENSE,
            categoryId = 1
        )

        mockMvc.perform(
            post("/transaction")
                .content(mapper.writeValueAsString(createtransaction))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.title").value(createtransaction.title))
            .andExpect(jsonPath("\$.id").isNotEmpty)
    }

    @Test
    fun createTransactionShouldBeUnprocessableEntityWhenInvalidCategory() {
        `when`(categoryRepository.findById(anyLong())).thenReturn(Optional.empty())
        val createtransactionFixture = CreateTransactionRequest(
            title = "Test",
            description = "Description test",
            value = 15.0,
            type = TransactionType.RECIVE,
            categoryId = 11
        )

        mockMvc.perform(
            post("/transaction")
                .content(mapper.writeValueAsString(createtransactionFixture))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.errors[0]").value("Categoria 11 não encontrada"))
    }

    @Test
    fun createTransactionShouldBeBadRequestWhenInvalidValue() {
        `when`(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryFixture))
        val createtransactionFixture = CreateTransactionRequest(
            title = "Test",
            description = "Description test",
            value = -10.0,
            type = TransactionType.RECIVE,
            categoryId = 1
        )

        mockMvc.perform(
            post("/transaction")
                .content(mapper.writeValueAsString(createtransactionFixture))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.errors[0]").exists())
    }

    @Test
    fun createTransactionShouldBeBadRequestWhenInvalidTitle() {
        `when`(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryFixture))
        val createtransactionFixture = CreateTransactionRequest(
            title = "",
            description = "Description test",
            value = 10.0,
            type = TransactionType.EXPENSE,
            categoryId = 1
        )

        mockMvc.perform(
            post("/transaction")
                .content(mapper.writeValueAsString(createtransactionFixture))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.errors[0]").exists())
    }

    companion object {
        private val categoryFixture = CategoryEntity(
            id = 1,
            description = "Category test",
            createdAt = LocalDateTime.now()
        )
        private val userFixture = UserEntity(id = 2, name = "Test", email = "test@test.com", password = "test1")


        private val transactionFixture = TransactionEntity(
            id = 1,
            title = "Test",
            description = "Description test",
            value = 15.0,
            type = TransactionType.EXPENSE,
            category = categoryFixture,
            user = userFixture,
            createdAt = LocalDateTime.now()
        )
    }
}
