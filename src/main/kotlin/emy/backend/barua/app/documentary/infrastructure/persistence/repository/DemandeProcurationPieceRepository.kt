package emy.backend.barua.app.documentary.infrastructure.persistence.repository

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DemandeProcurationPieceEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DemandeProcurationPieceRepository : CoroutineCrudRepository<DemandeProcurationPieceEntity, Long> {
    fun findByDemandeProcurationIdIn(demandeProcurationIds: List<Long>): Flow<DemandeProcurationPieceEntity>
}
