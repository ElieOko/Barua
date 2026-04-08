package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository


interface AccountRepository : CoroutineCrudRepository<AccountEntity, Long>