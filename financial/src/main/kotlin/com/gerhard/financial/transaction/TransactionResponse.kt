package com.gerhard.financial.transaction

import com.gerhard.financial.category.CategoryResponse
import java.time.LocalDateTime

data class TransactionResponse(
    val id: Long?,
    val title: String,
    val description: String?,
    val value: Double,
    val type: TransactionType,
    val category: CategoryResponse,
    val createdAt: LocalDateTime

) {
    constructor(transaction: TransactionEntity) : this(
        id = transaction.id,
        title = transaction.title,
        description = transaction.description,
        value = transaction.value,
        type = transaction.type,
        category = CategoryResponse(
            id = transaction.category.id,
            description = transaction.category.description,
            createdAt = transaction.category.createdAt
        ),
        createdAt = transaction.createdAt

    )
}