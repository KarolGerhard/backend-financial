package com.gerhard.financial.category

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class CategoryDTORequest(
    @JsonProperty("description")
    @field:NotBlank(message = "O descrição deve ser informada")
    var description: String
)

