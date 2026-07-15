package emy.backend.barua.app.user.application.services

import emy.backend.barua.app.organism.application.service.OrganismService
import emy.backend.barua.app.user.domain.models.*
import emy.backend.barua.app.user.domain.models.request.AssignAccountUserRequest
import emy.backend.barua.app.user.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.user.infrastructure.persistance.repositories.AccountUserRepository
import emy.backend.barua.app.user.infrastructure.persistance.repositories.UserRepository
import emy.backend.barua.utils.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*

@Service
@Profile(Mode.DEV)
class AccountUserService(
    private val repository: AccountUserRepository,
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val organismService: OrganismService,
) {
    suspend fun save(data: AccountUser): AccountUser {
        val result = repository.save(data.toEntity())
        return result.toDomain()
    }

    suspend fun assignRole(request: AssignAccountUserRequest): AccountUserDto {
        userRepository.findById(request.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable.")
        accountService.findByIdAccount(request.accountId)
        if (request.organismId != null) {
            organismService.findById(request.organismId)
        }
        repository.findByUserIdAndAccountIdAndOrganismId(
            userId = request.userId,
            accountId = request.accountId,
            organismId = request.organismId,
        )?.let {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cet utilisateur possède déjà ce rôle pour cet organisme.",
            )
        }
        return save(
            AccountUser(
                userId = request.userId,
                accountId = request.accountId,
                organismId = request.organismId,
            ),
        ).toDto()
    }

    suspend fun getAll() = repository.findAll().map { it.toDomain() }

    suspend fun findByIdAccount(id: Long): AccountUser {
        val data = repository.findById(id)
            ?: throw ResponseStatusException(HttpStatusCode.valueOf(404), "ID Is Not Found.")
        return data.toDomain()
    }

    suspend fun findMultipleAccountUser(userId: Long) = coroutineScope {
        repository.findAllAccountByUserId(userId).toList()
    }
}
