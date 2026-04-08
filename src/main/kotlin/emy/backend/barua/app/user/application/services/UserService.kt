package emy.backend.barua.app.user.application.services

import emy.backend.barua.app.user.domain.models.*
import emy.backend.barua.app.user.domain.models.request.*
import emy.backend.barua.app.user.infrastructure.entities.*
import emy.backend.barua.app.user.infrastructure.mapper.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.*
import emy.backend.barua.app.user.infrastructure.repositories.*
import emy.backend.barua.security.*
import emy.backend.barua.utils.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
import kotlin.time.*

@Service
@Profile(Mode.DEV)
class UserService(
    private val repository: UserRepository,
    private val service: TypeAccountService,
    private val auth: Auth
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    val name = "utilisateur"
    @OptIn(ExperimentalTime::class)
    suspend fun createUser(user: User) : UserDto? {
        val entityToSave = UserEntity(
            password = user.password,
            email = user.email,
            phone = user.phone,
            username = user.username,
            city = user.city,
            country = user.country
        )
        val savedEntity = repository.save(entityToSave)
        return savedEntity.toDomain()
    }

    suspend fun findAllUser() : Flow<UserDto> {
        val allEntityUser = repository.findAll()
        return allEntityUser.map { it.toDomain() }
    }

    suspend fun findIdUser(id : Long) : UserDto = coroutineScope {
        val userEntity = repository.findById(id)?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "ID Is Not Found for User with ID $id."
        )
        userEntity.toDomain()
//        }?: throw EntityNotFoundException("Aucun $name avec cet identifiant $id")

    }
    suspend fun findUsernameOrEmail(identifier : String): UserDto? {
        return  repository.findByPhoneOrEmail(identifier)?.toDomain()
    }

    suspend fun findId(id : Long) : UserDto? {
        val userEntity = repository.findById(id)
        return userEntity?.toDomain()
    }


    @OptIn(ExperimentalTime::class)
    suspend fun updateUser(
        id: Long,
        user: UserRequestChange
    ): UserDto ?{
      val userState =  repository.findById(id)
      if (userState?.email == user.email) {
          userState.city = user.city
          val updatedUser = repository.save(userState)
          return updatedUser.toDomain()
      }
      else{
          val state = repository.findByPhoneOrEmail(user.email)
          if(state != null) {
              throw ResponseStatusException(HttpStatus.CONFLICT, "Cette adresse email est déjà utilisé.")
          }
          userState?.email = user.email
          userState?.city = user.city
          val updatedUser = repository.save(userState!!)
          return updatedUser.toDomain()
      }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun updateUsername(
        id: Long,
        username : String
    ): UserDto ?{
        val userState =  repository.findById(id)
        if (userState != null) {
            userState.username = username
            val updatedUser = repository.save(userState)
            return updatedUser.toDomain()
        }
        return null
    }

    suspend fun deleteUser(id : Long) : Boolean{
        if (!repository.existsById(id)){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun $name avec cet identifiant $id")
            //NotFoundException("Aucun $name avec cet identifiant $id")
        }
        repository.deleteById(id)
        return true
    }
//    suspend fun findPersonByUser(userId : Long) = coroutineScope{
//        personService.findByIdPersonUser(userId)
//    }
    suspend fun isAdmin():Pair<Boolean, Long?>{
        val user = auth.user()
        val state = user?.second?.find { true } == true
        val id = user?.first?.userId ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Authorization denied, please login"
        )
        return state to id
    }
}