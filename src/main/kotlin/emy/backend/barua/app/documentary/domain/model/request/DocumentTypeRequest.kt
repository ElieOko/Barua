package emy.backend.barua.app.documentary.domain.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class DocumentTypeRequest(
    @field:NotBlank(message = "Le nom du type est obligatoire")
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
)
