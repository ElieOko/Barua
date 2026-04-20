package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.*
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class DemandeProcurationService(
    private val repository: DemandeProcurationRepository,
    private val serviceDocumentaireService: ServiceDocumentaireService,
) {
    suspend fun save(data: DemandeProcuration): DemandeProcuration? {
        serviceDocumentaireService.findById(data.serviceDocumentaireId)
        val saved = repository.save(data.toEntity())
        return saved.toDomain()
    }

    suspend fun findById(demandeId: Long?): DemandeProcuration? {
        if (demandeId == null) return null
        return repository.findById(demandeId)?.toDomain()
    }

    suspend fun findAll() = coroutineScope { repository.findAll().toList() }
}
