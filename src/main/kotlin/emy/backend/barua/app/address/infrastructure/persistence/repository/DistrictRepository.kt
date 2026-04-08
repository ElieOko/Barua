package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.DistrictEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DistrictRepository : CoroutineCrudRepository<DistrictEntity, Long>