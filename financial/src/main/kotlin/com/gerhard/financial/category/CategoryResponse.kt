package com.gerhard.financial.category

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CategoryResponse(
    @JsonProperty("id")
    var id: Long?,
    @JsonProperty("description")
    var description: String,
    @JsonProperty("createdAt")
    var createdAt: LocalDateTime
) {
    constructor(category: CategoryEntity) : this(
        id = category.id,
        description = category.description,
        createdAt = category.createdAt
    )
}
