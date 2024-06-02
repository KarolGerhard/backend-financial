package com.gerhard.financial.transaction

import com.gerhard.financial.category.CategoryRepository
import com.gerhard.financial.exceptions.ApplicationException
import com.gerhard.financial.exceptions.CategoryNotFoundException
import com.gerhard.financial.security.UserToken
import com.gerhard.financial.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(
    var transactionRepository: TransactionRepository,
    var categoryRepository: CategoryRepository,
    var userRepository: UserRepository
) {

    fun createTransaction(newTransaction: CreateTransactionRequest, userToken: UserToken): TransactionResponse {
        log.info("Criando transação para usuário= {}", userToken.email)
        val category = categoryRepository.findByIdOrNull(newTransaction.categoryId)
            ?: throw CategoryNotFoundException("Categoria ${newTransaction.categoryId} não encontrada")
        log.info("Encontrada categoria {}", category)

        val user = userRepository.findByIdOrNull(userToken.id)
            ?: throw ApplicationException("Usuário ${userToken.id} não encontrado")

        return transactionRepository.save(
            TransactionEntity(
                id = null,
                title = newTransaction.title!!,
                value = newTransaction.value!!,
                description = newTransaction.description,
                category = category,
                user = user,
                type = newTransaction.type,
                createdAt = LocalDateTime.now()
            )
        ).let { TransactionResponse(it) }
            .also { log.info("Criado transação {} com sucesso no banco", it.title) }
    }

    fun updateTransaction(
        newTransaction: CreateTransactionRequest,
        id: Long,
        userToken: UserToken
    ): TransactionResponse {
        log.info("Atualizando transação {} para usuário= {}", id, userToken.email)
        val category = categoryRepository.findByIdOrNull(newTransaction.categoryId)
            ?: throw CategoryNotFoundException("Categoria ${newTransaction.categoryId} não encontrada")
        log.info("Encontrada categoria {}", category)

        val user = userRepository.findByIdOrNull(userToken.id)
            ?: throw ApplicationException("Usuário ${userToken.id} não encontrado")

        val transaction = transactionRepository.findByIdOrNull(id)
            ?: throw ApplicationException("Transação $id não encontrado")

        if (transaction.user.id != userToken.id)
            throw ApplicationException("Transação $id não encontrada para o usuário ${userToken.id}")

        return transactionRepository.save(
            TransactionEntity(
                id = transaction.id,
                title = newTransaction.title!!,
                value = newTransaction.value!!,
                description = newTransaction.description,
                category = category,
                user = user,
                type = newTransaction.type,
                createdAt = transaction.createdAt
            )
        ).let { TransactionResponse(it) }
            .also { log.info("Atualizada transação {} com sucesso no banco", it.title) }
    }

    fun getTransactions(filter: TransactionFilter): List<TransactionResponse> {
        val transactions = filter.type?.let { t ->
            transactionRepository.findByUserIdAndType(filter.userToken!!.id, t)
        } ?: transactionRepository.findByUserId(filter.userToken!!.id)


        return transactions.map { TransactionResponse(it!!) }
            .let {
                when (filter.sortDir!!) {
                    SortDir.ASC -> it.sortedBy { t -> t.createdAt }
                    SortDir.DESC -> it.sortedByDescending { t -> t.createdAt }
                }
            }
            .also { log.info("Encontrado {} items", it.size) }
    }

    fun deleteTransaction(id: Long, userToken: UserToken) {
        val transaction = transactionRepository.findByIdOrNull(id)
            ?: throw ApplicationException("Transação $id não encontrado")

        if (transaction.user.id != userToken.id)
            throw ApplicationException("Transação $id não encontrada para o usuário ${userToken.id}")

        transactionRepository.delete(transaction)
    }


    companion object {
        val log = LoggerFactory.getLogger(TransactionService::class.java)
    }
}