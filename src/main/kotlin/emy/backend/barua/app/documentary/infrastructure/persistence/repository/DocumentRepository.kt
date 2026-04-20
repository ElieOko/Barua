package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DocumentEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DocumentRepository : CoroutineCrudRepository<DocumentEntity, Long> {
    fun findByDocumentTypeId(documentTypeId: Long): Flow<DocumentEntity>
}
