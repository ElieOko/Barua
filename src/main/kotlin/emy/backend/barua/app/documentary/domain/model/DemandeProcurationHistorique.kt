package emy.backend.barua.app.documentary.domain.model

import java.time.LocalDateTime

data class DemandeProcurationHistorique(
    val historiqueId: Long? = null,
    val demandeProcurationId: Long,
    val statusId: Long,
    val previousStatusId: Long? = null,
    val note: String? = null,
    val changedByUserId: Long? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

data class DemandeProcurationHistoriqueDto(
    val historiqueId: Long?,
    val demande: DemandeProcurationDto,
    val status: DemandeProcurationStatusDto,
    val previousStatus: DemandeProcurationStatusDto? = null,
    val note: String? = null,
    val changedAt: LocalDateTime,
    val changedByUserId: Long? = null,
)
