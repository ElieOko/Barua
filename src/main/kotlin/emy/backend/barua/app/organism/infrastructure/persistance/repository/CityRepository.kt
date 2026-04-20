package emy.backend.barua.app.organism.infrastructure.persistance.repository

import emy.backend.barua.app.organism.infrastructure.persistance.entities.CityEntity
import emy.backend.barua.app.organism.infrastructure.persistance.entities.OrganismEntity
import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CityRepository : CoroutineCrudRepository<CityEntity, Long>