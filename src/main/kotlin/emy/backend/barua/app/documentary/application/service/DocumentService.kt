package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.*
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DocumentRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DocumentService(
    private val repository: DocumentRepository,
) {
    suspend fun save(data: Document): Document? {
        val saved = repository.save(data.toEntity())
        return saved.toDomain()
    }

    suspend fun findAll() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByDocumentTypeId(documentTypeId: Long) =
        repository.findByDocumentTypeId(documentTypeId).map { it.toDomain() }.toList()

    suspend fun findById(id: Long): Document = coroutineScope {
         repository.findById(id)?.toDomain()?:throw ResponseStatusException(
             HttpStatusCode.valueOf(404),
             "Document Not Found with ID $id."
         )
    }
    suspend fun update(id: Long, data: Document): Document? {
        if (repository.findById(id) == null) return null
        val updated = data.copy(documentId = id)
        return repository.save(updated.toEntity()).toDomain()
    }
}
