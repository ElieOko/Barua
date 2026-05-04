package emy.backend.barua.app.user.application.services

import emy.backend.barua.app.user.domain.models.*
import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountDTO
import emy.backend.barua.app.user.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.user.infrastructure.persistance.repositories.AccountRepository
import emy.backend.barua.utils.*
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*

@Service
@Profile(Mode.DEV)
class AccountService(
    private val repository: AccountRepository,
    private val typeAccountService: TypeAccountService
) {
    suspend fun save(data: Account): Account {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun getAll() = repository.findAll().map {
        AccountDTO(
            id = it.id,
            name = it.name,
            typeAccount = typeAccountService.findByIdTypeAccount(it.typeAccountId)
        )
    }

    suspend fun findByIdAccount(id : Long): Account {
      val data = repository.findById(id)?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "ID Is Not Found.")
        return data.toDomain()
    }
    suspend fun findAccountWithType(account : Long, type : Long): Account {
      val data = repository.findAll().filter { it.typeAccountId == account && it.typeAccountId == type }.toList()
      if (data.isEmpty()) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Ce compte et type ne sont pas prise en charge.")
      return data.first().toDomain()
    }
    suspend fun isAllow(accountId : Long):Boolean = accountId == 5L
    suspend fun isAllowCommune(accountId : Long):Boolean = accountId == 3L
    suspend fun isAllowMinistere(accountId : Long):Boolean = accountId == 4L
}