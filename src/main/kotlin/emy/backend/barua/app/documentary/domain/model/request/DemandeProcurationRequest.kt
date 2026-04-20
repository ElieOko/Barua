package emy.backend.barua.app.documentary.domain.model.request

import jakarta.validation.constraints.NotNull

data class DemandeProcurationRequest(
    @field:NotNull(message = "Le service documentaire est obligatoire")
    val serviceDocumentaireId: Long,
    val commentaire: String? = null,
)
