package emy.backend.barua.app.organism.infrastructure.persistance.repository

import emy.backend.barua.app.organism.infrastructure.persistance.entities.OrganismEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OrganismRepository : CoroutineCrudRepository<OrganismEntity, Long>{
    @Query("SELECT * FROM organisms WHERE city_id = :cityId AND type_id = :typeId")
    fun findByCityIdAndTypeId(cityId : Long, typeId : Long) : Flow<OrganismEntity>?
}