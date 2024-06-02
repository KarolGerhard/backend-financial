package com.gerhard.financial.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, Long> {

    @Query(
        "select count(t) from transactions t" +
                " join t.category c" +
                " where c.id = :categoryId"
    )
    fun countByCategory(categoryId: Long): Long


    @Query(
        "select t from transactions t" +
                " join t.category c" +
                " join t.user u" +
                " where u.id = :userId"
    )
    fun findByUserId(userId: Long): List<TransactionEntity?>

    @Query(
        "select t from transactions t" +
                " join t.category c" +
                " join t.user u" +
                " where t.type = :type and u.id = :userId"
    )
    fun findByUserIdAndType(userId: Long, type: TransactionType): List<TransactionEntity?>


}

