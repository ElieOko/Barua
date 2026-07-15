package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.DemandeProcurationHistorique
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("demande_procuration_status_history")
data class DemandeProcurationHistoriqueEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("demande_procuration_id")
    val demandeProcurationId: Long,
    @Column("status_id")
    val statusId: Long,
    @Column("previous_status_id")
    val previousStatusId: Long? = null,
    @Column("note")
    val note: String? = null,
    @Column("changed_by_user_id")
    val changedByUserId: Long? = null,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun DemandeProcurationHistoriqueEntity.toDomain() = DemandeProcurationHistorique(
    historiqueId = id,
    demandeProcurationId = demandeProcurationId,
    statusId = statusId,
    previousStatusId = previousStatusId,
    note = note,
    changedByUserId = changedByUserId,
    createdAt = createdAt,
)

fun DemandeProcurationHistorique.toEntity() = DemandeProcurationHistoriqueEntity(
    id = historiqueId,
    demandeProcurationId = demandeProcurationId,
    statusId = statusId,
    previousStatusId = previousStatusId,
    note = note,
    changedByUserId = changedByUserId,
    createdAt = createdAt,
)
