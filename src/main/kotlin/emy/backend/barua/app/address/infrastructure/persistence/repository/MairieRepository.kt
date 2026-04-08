package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.MairieEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MairieRepository : CoroutineCrudRepository<MairieEntity,Long>