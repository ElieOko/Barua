package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.CommuneEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CommuneRepository : CoroutineCrudRepository<CommuneEntity,Long>