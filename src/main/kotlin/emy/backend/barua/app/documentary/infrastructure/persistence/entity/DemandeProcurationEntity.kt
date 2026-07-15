package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.DemandeProcuration
import emy.backend.barua.app.documentary.domain.model.DemandeProcurationPiece
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("demande_procuration")
data class DemandeProcurationEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("user_id")
    val userId: Long,
    @Column("service_documentaire_id")
    val serviceDocumentaireId: Long,
    @Column("status_id")
    val statusId: Long,
    @Column("fullName")
    val fullName: String? = null,
    @Column("numberIdentity")
    val numberIdentity: String? = null,
    @Column("phone")
    val phone: String? = null,
    @Column("commentaire")
    val commentaire: String? = null,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now(),
)

fun DemandeProcurationEntity.toDomain(
    pieces: List<DemandeProcurationPiece> = emptyList(),
) = DemandeProcuration(
    demandeId = this.id,
    userId = this.userId,
    serviceDocumentaireId = this.serviceDocumentaireId,
    statusId = this.statusId,
    commentaire = this.commentaire,
    fullName = this.fullName,
    numberIdentity = this.numberIdentity,
    phone = this.phone,
    pieces = pieces,
)
