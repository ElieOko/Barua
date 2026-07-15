package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.*
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationStatusRepository
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DemandeProcurationStatusService(
    private val repository: DemandeProcurationStatusRepository,
) {
    suspend fun findAllActive(): List<DemandeProcurationStatusDto> =
        repository.findByIsActiveTrueOrderByIdAsc()
            .toList()
            .map { it.toDomain().toDto() }

    suspend fun findByCode(code: String): DemandeProcurationStatus {
        val normalized = code.trim().lowercase()
        val entity = repository.findByCode(normalized)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Statut '$code' introuvable.")
        return entity.toDomain()
    }

    suspend fun findById(statusId: Long): DemandeProcurationStatus {
        val entity = repository.findById(statusId)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Statut introuvable.")
        return entity.toDomain()
    }

    suspend fun findByIdIn(statusIds: List<Long>): Map<Long, DemandeProcurationStatusDto> {
        if (statusIds.isEmpty()) return emptyMap()
        return statusIds.distinct()
            .mapNotNull { repository.findById(it)?.toDomain()?.toDto() }
            .associateBy { it.statusId }
    }
}
