package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.DocumentType
import emy.backend.barua.app.documentary.domain.model.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DocumentTypeRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class DocumentTypeService(
    private val repository: DocumentTypeRepository,
) {
    suspend fun save(data: DocumentType): DocumentType? {
        val saved = repository.save(data.toEntity())
        return saved.toDomain()
    }

    suspend fun findAll() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findById(id: Long?): DocumentType? {
        if (id == null) return null
        return repository.findById(id)?.toDomain()
    }

    suspend fun update(id: Long, data: DocumentType): DocumentType? {
        if (repository.findById(id) == null) return null
        val updated = data.copy(documentTypeId = id)
        return repository.save(updated.toEntity()).toDomain()
    }
}
