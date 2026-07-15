package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.DemandeProcurationPiece
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("demande_procuration_pieces")
data class DemandeProcurationPieceEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("demande_procuration_id")
    val demandeProcurationId: Long,
    @Column("name")
    val name: String,
    @Column("path")
    val path: String,
    @Column("content_type")
    val contentType: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun DemandeProcurationPieceEntity.toDomain() = DemandeProcurationPiece(
    pieceId = id,
    demandeProcurationId = demandeProcurationId,
    name = name,
    path = path,
    contentType = contentType,
)
