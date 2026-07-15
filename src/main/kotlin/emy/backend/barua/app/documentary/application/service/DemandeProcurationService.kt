package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.*
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class DemandeProcurationService(
    private val repository: DemandeProcurationRepository,
    private val serviceDocumentaireService: ServiceDocumentaireService,
    private val pieceService: DemandeProcurationPieceService,
    private val statusService: DemandeProcurationStatusService,
    private val historiqueService: DemandeProcurationHistoriqueService,
) {
    suspend fun save(data: DemandeProcuration, files: List<MultipartFile> = emptyList()): DemandeProcurationDto? {
        serviceDocumentaireService.findById(data.serviceDocumentaireId)
        val saved = repository.save(data.toEntity())
        val demandeId = saved.id ?: return null
        val pieces = pieceService.saveAll(demandeId, files)
        historiqueService.recordChange(
            demandeProcurationId = demandeId,
            statusId = saved.statusId,
            note = "Demande soumise",
        )
        return toDto(saved, pieces)
    }

    suspend fun findById(demandeId: Long?): DemandeProcurationDto? {
        if (demandeId == null) return null
        val entity = repository.findById(demandeId) ?: return null
        val pieces = pieceService.findByDemandeProcurationIdIn(listOf(demandeId))
        return toDto(entity, pieces)
    }

    suspend fun findByIds(demandeIds: List<Long>): List<DemandeProcurationDto> = coroutineScope {
        if (demandeIds.isEmpty()) return@coroutineScope emptyList()
        enrichWithRelations(demandeIds.mapNotNull { repository.findById(it) })
    }

    suspend fun findAll() = coroutineScope {
        enrichWithRelations(repository.findAll().toList())
    }

    suspend fun findByUserId(userId: Long) = coroutineScope {
        enrichWithRelations(repository.findByUserIdOrderByCreatedAtDesc(userId).toList())
    }

    suspend fun changeStatus(
        demandeId: Long,
        statusCode: String,
        note: String? = null,
        changedByUserId: Long? = null,
    ): DemandeProcurationDto {
        val entity = repository.findById(demandeId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Demande introuvable.")
        val newStatus = statusService.findByCode(statusCode)
        if (entity.statusId == newStatus.statusId) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "La demande possède déjà ce statut.")
        }
        val previousStatusId = entity.statusId
        val updated = repository.save(
            entity.copy(
                statusId = newStatus.statusId,
                updatedAt = LocalDateTime.now(),
            ),
        )
        historiqueService.recordChange(
            demandeProcurationId = demandeId,
            statusId = newStatus.statusId,
            previousStatusId = previousStatusId,
            note = note,
            changedByUserId = changedByUserId,
        )
        val pieces = pieceService.findByDemandeProcurationIdIn(listOf(demandeId))
        return toDto(updated, pieces)
    }

    suspend fun ensureOwnedByUser(demandeId: Long, userId: Long): DemandeProcurationEntity {
        val entity = repository.findById(demandeId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Demande introuvable.")
        if (entity.userId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé à cette demande.")
        }
        return entity
    }

    private suspend fun enrichWithRelations(entities: List<DemandeProcurationEntity>): List<DemandeProcurationDto> {
        if (entities.isEmpty()) return emptyList()
        val ids = entities.mapNotNull { it.id }
        val piecesByDemandeId = pieceService.findByDemandeProcurationIdIn(ids)
            .groupBy { it.demandeProcurationId }
        val statusIds = entities.map { it.statusId }.distinct()
        val statusesById = statusService.findByIdIn(statusIds)
        return entities.map { entity ->
            val status = statusesById[entity.statusId]
                ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Statut introuvable.")
            entity.toDto(status, piecesByDemandeId[entity.id].orEmpty())
        }
    }

    private suspend fun toDto(
        entity: DemandeProcurationEntity,
        pieces: List<DemandeProcurationPiece>,
    ): DemandeProcurationDto {
        val status = statusService.findById(entity.statusId).toDto()
        return entity.toDto(status, pieces)
    }
}
