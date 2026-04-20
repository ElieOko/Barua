package emy.backend.barua.app.documentary.domain.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class DocumentRequest(
    @field:NotNull(message = "Le type de document est obligatoire")
    val documentTypeId: Long,
    @field:NotBlank(message = "Le titre est obligatoire")
    val title: String,
    val code: String? = null,
    val description: String? = null,
    val isActive: Boolean = true,
)
