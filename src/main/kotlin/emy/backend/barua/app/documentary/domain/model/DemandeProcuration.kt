package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationEntity

data class DemandeProcuration(
    var demandeId: Long? = null,
    val userId: Long,
    val serviceDocumentaireId: Long,
    val statusId: Long,
    val commentaire: String? = null,
    val fullName: String? = null,
    val numberIdentity: String? = null,
    val phone: String? = null,
    val pieces: List<DemandeProcurationPiece> = emptyList(),
)

fun DemandeProcuration.toEntity() = DemandeProcurationEntity(
    id = this.demandeId,
    userId = this.userId,
    serviceDocumentaireId = this.serviceDocumentaireId,
    statusId = this.statusId,
    commentaire = this.commentaire,
    fullName = this.fullName,
    numberIdentity = this.numberIdentity,
    phone = this.phone,
)
