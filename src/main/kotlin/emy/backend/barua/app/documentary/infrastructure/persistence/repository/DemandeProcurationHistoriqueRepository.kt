package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationHistoriqueEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DemandeProcurationHistoriqueRepository : CoroutineCrudRepository<DemandeProcurationHistoriqueEntity, Long> {
    fun findByDemandeProcurationIdOrderByCreatedAtAsc(demandeProcurationId: Long): Flow<DemandeProcurationHistoriqueEntity>
    fun findAllByOrderByCreatedAtDesc(): Flow<DemandeProcurationHistoriqueEntity>
}
