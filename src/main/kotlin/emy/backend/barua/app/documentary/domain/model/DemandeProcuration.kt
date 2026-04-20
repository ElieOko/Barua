package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationEntity

data class DemandeProcuration(
    var demandeId: Long? = null,
    val userId: Long,
    val serviceDocumentaireId: Long,
    val status: String = StatutDemandeProcuration.EN_ATTENTE.name,
    val commentaire: String? = null,
)

fun DemandeProcuration.toEntity() = DemandeProcurationEntity(
    id = this.demandeId,
    userId = this.userId,
    serviceDocumentaireId = this.serviceDocumentaireId,
    status = this.status,
    commentaire = this.commentaire
)