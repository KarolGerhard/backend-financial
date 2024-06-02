package com.gerhard.financial.transaction

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateTransactionRequest(
    @field:NotBlank(message = "O título deve ser informado")
    val title: String?,

    val description: String?,

    @field:NotNull(message = "O tipo deve ser informado")
    val type: TransactionType,

    @field:NotNull(message = "Deve ser informado um valor maior que zero.")
    @field:Min(value = 1, message = "Deve ser informado um valor maior que zero.")
    val value: Double?,

    @field:NotNull(message = "Deve ser informado uma categoria válida")
    @field:Min(value = 1, message = "Deve ser informado uma categoria válida")
    val categoryId: Long?
)