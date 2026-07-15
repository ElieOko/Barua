package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationPieceEntity

data class DemandeProcurationPiece(
    val pieceId: Long? = null,
    val demandeProcurationId: Long,
    val name: String,
    val path: String,
    val contentType: String? = null,
)

fun DemandeProcurationPiece.toEntity() = DemandeProcurationPieceEntity(
    id = pieceId,
    demandeProcurationId = demandeProcurationId,
    name = name,
    path = path,
    contentType = contentType,
)
