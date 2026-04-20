package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaire
import emy.backend.barua.app.documentary.domain.model.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.ServiceDocumentaireRepository
import emy.backend.barua.app.organism.application.service.OrganismService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ServiceDocumentaireService(
    private val repository: ServiceDocumentaireRepository,
    private val documentService: DocumentService,
    private val organismService: OrganismService,
) {
    suspend fun save(data: ServiceDocumentaire): ServiceDocumentaire? {
        documentService.findById(data.documentId)
        organismService.findById(data.organismId)
        val saved = repository.save(data.toEntity())
        return saved.toDomain()
    }

    suspend fun findAll() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByDocumentId(documentId: Long) =
        repository.findByDocumentId(documentId).map { it.toDomain() }.toList()

    suspend fun findById(id: Long): ServiceDocumentaire = coroutineScope {
        repository.findById(id)?.toDomain()?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Document Not Found with ID $id."
        )
    }

    suspend fun update(id: Long, data: ServiceDocumentaire): ServiceDocumentaire? {
        if (repository.findById(id) == null) return null
        documentService.findById(data.documentId)
        organismService.findById(data.organismId)
        val updated = data.copy(serviceDocumentaireId = id)
        return repository.save(updated.toEntity()).toDomain()
    }
}
