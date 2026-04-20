package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DemandeProcurationRepository : CoroutineCrudRepository<DemandeProcurationEntity, Long> {
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): Flow<DemandeProcurationEntity>
}
