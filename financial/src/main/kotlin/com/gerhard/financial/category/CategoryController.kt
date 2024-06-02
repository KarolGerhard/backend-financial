package com.gerhard.financial.category

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/category")
class CategoryController(private var categoryService: CategoryService) {

    @PostMapping
    fun createCategory(@RequestBody @Valid category: CategoryDTORequest): CategoryResponse {
        return categoryService.createCategory(category)
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @RequestBody @Valid category: CategoryDTORequest): CategoryResponse {
        return categoryService.updateCategory(category, id)
    }


    @GetMapping
    fun getCategories(): List<CategoryResponse> {
        return categoryService.getCategories()
    }

    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): CategoryResponse {
        return categoryService.getCategory(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada")
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long) {
        return categoryService.deleteCategory(id)
    }
}