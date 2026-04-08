package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.TypeAccountEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TypeAccountRepository : CoroutineCrudRepository<TypeAccountEntity, Long>