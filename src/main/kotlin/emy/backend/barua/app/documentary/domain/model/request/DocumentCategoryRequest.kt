package emy.backend.barua.app.documentary.domain.model.request

import jakarta.validation.constraints.NotBlank

data class DocumentCategoryRequest(
    @field:NotBlank(message = "Le nom de la catégorie est obligatoire")
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
)
