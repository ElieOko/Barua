package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.QuartierEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface QuartierRepository : CoroutineCrudRepository<QuartierEntity,Long>