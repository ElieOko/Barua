package emy.backend.barua.app.documentary.domain.model.request

import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaire
import jakarta.validation.constraints.NotNull

data class ServiceDocumentaireRequest(
    @field:NotNull(message = "Le document est obligatoire")
    val documentId: Long,
    @field:NotNull(message = "L'identifiant de l'organisme est obligatoire")
    val organismId: Long,
    @field:NotNull(message = "Le prix est obligatoire")
    val price: Double,
    @field:NotNull(message = "La devise est obligatoire")
    val devise: Long,
    @field:NotNull(message = "Le délai en jours ouvrables est obligatoire")
    val delayDayOpen: String,
    val description: String? = null,
)

fun ServiceDocumentaireRequest.toDomain(userId : Long)= ServiceDocumentaire(
    documentId = this.documentId,
    organismId = this.organismId,
    userId = userId,
    price = this.price,
    deviseId = this.devise,
    description = this.description,
    delayDayOpen = this.delayDayOpen,
)