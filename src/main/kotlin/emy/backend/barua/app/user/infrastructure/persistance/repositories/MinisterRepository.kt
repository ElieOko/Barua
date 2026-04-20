package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.MinisterEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MinisterRepository : CoroutineCrudRepository<MinisterEntity, Long>
