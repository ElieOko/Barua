package emy.backend.barua.app.documentary.domain.model.request

import emy.backend.barua.app.organism.domain.model.OrganismType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class ServiceDocumentaireRequest(
    @field:NotNull(message = "Le document est obligatoire")
    val documentId: Long,
    @field:NotNull(message = "Le type d'organisme émetteur est obligatoire")
    val emetteurType: OrganismType,
    @field:NotNull(message = "L'identifiant de l'organisme est obligatoire")
    val organismId: Long,
    @field:NotNull(message = "Le prix est obligatoire")
    val price: Double,
    @field:NotNull(message = "La devise est obligatoire")
    val devise: Long,
    @field:NotNull(message = "Le délai en jours ouvrables est obligatoire")
    val delayDayOpen: String,
    val description: String? = null,
    val isActive: Boolean = true,
)
