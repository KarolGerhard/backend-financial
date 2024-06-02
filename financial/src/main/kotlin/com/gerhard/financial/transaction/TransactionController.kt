package com.gerhard.financial.transaction

import com.gerhard.financial.security.UserToken
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transaction")
class TransactionController(var transactionService: TransactionService) {

    fun getUser(): UserToken {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.principal as UserToken
    }

    @PostMapping
    fun create(@RequestBody @Valid transaction: CreateTransactionRequest): TransactionResponse {
        return transactionService.createTransaction(transaction, getUser())
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid transaction: CreateTransactionRequest): TransactionResponse {
        return transactionService.updateTransaction(transaction, id, getUser())
    }

    @GetMapping
    fun getTransactions(
        @RequestParam type: String? = null,
        @RequestParam sortDir: String? = null
    ): List<TransactionResponse> {
        return SortDir.entries.firstOrNull { it.name == (sortDir ?: "DESC").uppercase() }
            .let { sort ->
                TransactionFilter(
                    type = TransactionType.entries.firstOrNull { it.name == (type ?: "").uppercase() },
                    sortDir = sort!!,
                    userToken = getUser()
                )
            }
            .let { transactionService.getTransactions(it) }
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        return transactionService.deleteTransaction(id, getUser())
    }
}


