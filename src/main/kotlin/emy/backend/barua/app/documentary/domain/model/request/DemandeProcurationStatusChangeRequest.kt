package emy.backend.barua.app.documentary.domain.model.request

import jakarta.validation.constraints.NotBlank

data class DemandeProcurationStatusChangeRequest(
    @field:NotBlank(message = "Le code du statut est obligatoire")
    val statusCode: String,
    val note: String? = null,
)
