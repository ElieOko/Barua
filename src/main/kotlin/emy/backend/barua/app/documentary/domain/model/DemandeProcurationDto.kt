package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationEntity
import java.time.LocalDateTime

data class DemandeProcurationDto(
    val demandeId: Long?,
    val userId: Long,
    val serviceDocumentaireId: Long,
    val status: DemandeProcurationStatusDto,
    val commentaire: String? = null,
    val fullName: String? = null,
    val numberIdentity: String? = null,
    val phone: String? = null,
    val pieces: List<DemandeProcurationPiece> = emptyList(),
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
)

fun DemandeProcurationEntity.toDto(
    status: DemandeProcurationStatusDto,
    pieces: List<DemandeProcurationPiece> = emptyList(),
) = DemandeProcurationDto(
    demandeId = id,
    userId = userId,
    serviceDocumentaireId = serviceDocumentaireId,
    status = status,
    commentaire = commentaire,
    fullName = fullName,
    numberIdentity = numberIdentity,
    phone = phone,
    pieces = pieces,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
