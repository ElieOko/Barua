package emy.backend.barua.app.user.application.services

import emy.backend.barua.app.user.domain.models.*
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
import emy.backend.barua.utils.*
import emy.backend.barua.app.user.infrastructure.persistance.entities.TypeAccountEntity
import emy.backend.barua.app.user.infrastructure.persistance.mapper.toDomain
import emy.backend.barua.app.user.infrastructure.persistance.mapper.toEntity
import emy.backend.barua.app.user.infrastructure.persistance.repositories.TypeAccountRepository

@Service
@Profile(Mode.DEV)
class TypeAccountService(
    private val repository: TypeAccountRepository,
) {
    suspend fun saveAccount(data: TypeAccount): TypeAccount {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll(): Flow<TypeAccount> {
        val data= repository.findAll()
        return data.map {
            TypeAccountEntity(it.id, it.name).toDomain()
        }
    }
    suspend fun findByIdTypeAccount(id : Long) : TypeAccount {
      val data = repository.findById(id) ?: throw ResponseStatusException(
          HttpStatusCode.valueOf(404),
          "ID Is Not Found."
      )
        return data.toDomain()
    }
}