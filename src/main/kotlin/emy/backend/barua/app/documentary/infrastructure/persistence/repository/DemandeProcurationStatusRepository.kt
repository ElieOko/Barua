package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationStatusEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DemandeProcurationStatusRepository : CoroutineCrudRepository<DemandeProcurationStatusEntity, Long> {
    suspend fun findByCode(code: String): DemandeProcurationStatusEntity?
    fun findByIsActiveTrueOrderByIdAsc(): Flow<DemandeProcurationStatusEntity>
}
