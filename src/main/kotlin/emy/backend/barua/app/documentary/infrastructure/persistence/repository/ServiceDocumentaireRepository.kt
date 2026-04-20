package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.ServiceDocumentaireEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ServiceDocumentaireRepository : CoroutineCrudRepository<ServiceDocumentaireEntity, Long> {
    fun findByDocumentId(documentId: Long): Flow<ServiceDocumentaireEntity>
}
