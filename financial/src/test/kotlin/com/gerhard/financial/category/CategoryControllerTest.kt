package com.gerhard.financial.category

import com.fasterxml.jackson.databind.ObjectMapper
import com.gerhard.financial.security.Jwt
import com.gerhard.financial.user.UserEntity
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CategoryControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var jwt: Jwt

    @BeforeEach fun setup() = clearAllMocks()
    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    private val categoryFixture = CategoryEntity(
        id = 1,
        description = "Category test",
        createdAt = LocalDateTime.now()
    )

    private var token = "";

    @BeforeEach
    fun generateToken(){
        token = jwt.createToken(UserEntity(id = 1, name = "test", email = "test@test.com", password = ""))
    }

    @Test
    fun createCategoryShouldBeOkWhenValid() {
        `when`(categoryRepository.findByDescription(Mockito.anyString())).thenReturn(emptyList())
        `when`(categoryRepository.save(Mockito.any())).thenReturn(categoryFixture)

        val createCategoryRequest = CategoryDTORequest(description = "Category test")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/category")
                .content(mapper.writeValueAsString(createCategoryRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.description").value(categoryFixture.description))
            .andExpect(jsonPath("\$.id").isNotEmpty)
            .andExpect(jsonPath("\$.createdAt").isNotEmpty)
    }

    @Test
    fun createCategoryShouldBeUnprocessableEntityWhenCategoryHasExist() {
        `when`(categoryRepository.findByDescription(Mockito.anyString())).thenReturn(listOf(categoryFixture))

        val createCategoryRequest = CategoryDTORequest(description = "Category test")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/category")
                .content(mapper.writeValueAsString(createCategoryRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.errors[0]").value("Já existe uma categoria com as mesmas descrições"))
    }

    @Test
    fun listCategoryShouldBeValid() {
        `when`(categoryRepository.findAll()).thenReturn(listOf(categoryFixture))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
}