package com.gerhard.financial.category

import com.gerhard.financial.exceptions.ApplicationException
import com.gerhard.financial.exceptions.BadRequestException
import com.gerhard.financial.exceptions.CategoryHasRegisteredException
import com.gerhard.financial.exceptions.CategoryNotFoundException
import com.gerhard.financial.transaction.TransactionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class CategoryService(var categoryRepository: CategoryRepository, var transactionRepository: TransactionRepository) {

    fun createCategory(newCategory: CategoryDTORequest): CategoryResponse {
        checkIfExists(newCategory)

        val save = categoryRepository.save(
            CategoryEntity(
                id = null,
                description = newCategory.description,
                createdAt = LocalDateTime.now()
            )
        )
        return CategoryResponse(save)
    }

    fun updateCategory(newCategory: CategoryDTORequest, id: Long): CategoryResponse {
        val category = categoryRepository.findByIdOrNull(id)
            ?: throw CategoryNotFoundException("Categoria ${id} não encontrada")
        val save = categoryRepository.save(
            CategoryEntity(
                id = id,
                description = newCategory.description,
                createdAt = category.createdAt
            )
        )
        return CategoryResponse(save)
    }

    fun getCategory(id: Long): CategoryResponse? {
        return categoryRepository.findById(id)
            .map { CategoryResponse(it) }.getOrNull()
    }

    fun getCategories(): List<CategoryResponse> {
        return categoryRepository.findAll()
            .map { CategoryResponse(id = it.id, description = it.description, createdAt = it.createdAt) }
    }

    fun deleteCategory(id: Long) {
        val hasTransactions = transactionRepository.countByCategory(id) > 0
        if(hasTransactions) throw BadRequestException("Existem lançamentos para essa categoria")

        categoryRepository.deleteById(id)
    }

    private fun checkIfExists(newCategory: CategoryDTORequest) {
        val hasCategory = categoryRepository.findByDescription(newCategory.description).isNotEmpty()
        if (hasCategory) throw CategoryHasRegisteredException(
            message = "Já existe uma categoria com as mesmas descrições"
        )
    }
}

