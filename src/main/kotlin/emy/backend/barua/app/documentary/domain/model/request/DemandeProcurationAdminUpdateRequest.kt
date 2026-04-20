package emy.backend.barua.app.documentary.domain.model.request

import emy.backend.barua.app.documentary.domain.model.StatutDemandeProcuration
import jakarta.validation.constraints.NotNull

data class DemandeProcurationAdminUpdateRequest(
    @field:NotNull(message = "Le statut est obligatoire")
    val statut: StatutDemandeProcuration,
    val commentaireDemandeur: String? = null,
    /** Si renseigné, réaffecte la demande à un autre service documentaire (doit être actif). */
    val serviceDocumentaireId: Long? = null,
)
