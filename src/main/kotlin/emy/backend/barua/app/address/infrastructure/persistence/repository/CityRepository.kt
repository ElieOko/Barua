package emy.backend.barua.app.address.infrastructure.persistence.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import emy.backend.barua.app.address.infrastructure.persistence.entity.CityEntity

interface CityRepository : CoroutineCrudRepository<CityEntity, Long>