package com.gerhard.financial.transaction

import com.gerhard.financial.category.CategoryEntity
import com.gerhard.financial.user.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "transactions")
class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val title: String,
    val description: String?,
    @Column(name = "transaction_type")
    val type: TransactionType,
    @Column(name = "transaction_value")
    val value: Double,
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "category_id")
    val category: CategoryEntity,
    @ManyToOne(cascade = [CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    val createdAt: LocalDateTime
)

