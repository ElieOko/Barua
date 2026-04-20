package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.ServicePublicEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ServicePublicRepository : CoroutineCrudRepository<ServicePublicEntity, Long>
