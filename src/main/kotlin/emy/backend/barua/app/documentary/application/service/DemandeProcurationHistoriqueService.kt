package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.*
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationHistoriqueRepository
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class DemandeProcurationHistoriqueService(
    private val repository: DemandeProcurationHistoriqueRepository,
    private val demandeRepository: DemandeProcurationRepository,
    private val pieceService: DemandeProcurationPieceService,
    private val statusService: DemandeProcurationStatusService,
) {
    suspend fun recordChange(
        demandeProcurationId: Long,
        statusId: Long,
        previousStatusId: Long? = null,
        note: String? = null,
        changedByUserId: Long? = null,
    ): DemandeProcurationHistorique {
        val saved = repository.save(
            DemandeProcurationHistorique(
                demandeProcurationId = demandeProcurationId,
                statusId = statusId,
                previousStatusId = previousStatusId,
                note = note,
                changedByUserId = changedByUserId,
            ).toEntity(),
        )
        return saved.toDomain()
    }

    suspend fun findAllDto(): List<DemandeProcurationHistoriqueDto> {
        val entries = repository.findAllByOrderByCreatedAtDesc().toList()
        return toHistoriqueDtos(entries)
    }

    suspend fun findByDemandeIdDto(demandeProcurationId: Long): List<DemandeProcurationHistoriqueDto> {
        val entries = repository.findByDemandeProcurationIdOrderByCreatedAtAsc(demandeProcurationId).toList()
        return toHistoriqueDtos(entries)
    }

    private suspend fun toHistoriqueDtos(
        entries: List<emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationHistoriqueEntity>,
    ): List<DemandeProcurationHistoriqueDto> {
        if (entries.isEmpty()) return emptyList()

        val demandeIds = entries.map { it.demandeProcurationId }.distinct()
        val demandesById = buildDemandeDtos(demandeIds).associateBy { it.demandeId }

        val statusIds = entries.flatMap { listOfNotNull(it.statusId, it.previousStatusId) }.distinct()
        val statusesById = statusService.findByIdIn(statusIds)

        return entries.mapNotNull { entry ->
            val demande = demandesById[entry.demandeProcurationId] ?: return@mapNotNull null
            val status = statusesById[entry.statusId] ?: return@mapNotNull null
            DemandeProcurationHistoriqueDto(
                historiqueId = entry.id,
                demande = demande,
                status = status,
                previousStatus = entry.previousStatusId?.let { statusesById[it] },
                note = entry.note,
                changedAt = entry.createdAt,
                changedByUserId = entry.changedByUserId,
            )
        }
    }

    private suspend fun buildDemandeDtos(demandeIds: List<Long>): List<DemandeProcurationDto> {
        if (demandeIds.isEmpty()) return emptyList()
        val entities = demandeIds.mapNotNull { demandeRepository.findById(it) }
        val piecesByDemandeId = pieceService.findByDemandeProcurationIdIn(demandeIds)
            .groupBy { it.demandeProcurationId }
        val statusIds = entities.map { it.statusId }.distinct()
        val statusesById = statusService.findByIdIn(statusIds)
        return entities.map { entity ->
            val status = statusesById[entity.statusId]!!
            entity.toDto(status, piecesByDemandeId[entity.id].orEmpty())
        }
    }
}
