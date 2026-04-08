package emy.backend.barua.security

import emy.backend.barua.app.user.application.services.*
import emy.backend.barua.app.user.domain.models.*
import emy.backend.barua.app.user.infrastructure.mapper.*
import emy.backend.barua.app.user.infrastructure.repositories.*
import org.springframework.security.core.context.*
import org.springframework.stereotype.*
import java.security.*

@Service
class Auth(
    private val repository : UserRepository,
    private val account : AccountService,
    private val mutlipleAccount : AccountUserService
) {
    suspend fun user(): Pair<UserDto?, MutableList<Boolean>>?{
        val allowList = mutableListOf<Boolean>()
        SecurityContextHolder.getContext().authentication?.name?.let {
            val userId = it.toInt(16).toLong()
            val data = repository.findById(userId)
            mutlipleAccount.findMultipleAccountUser(userId).forEach{c->allowList.add(account.isAllow(c.accountId))}
            return Pair(data?.toDomain(),allowList)
        }
        return null
    }
    suspend fun userStom(principal: Principal): UserDto? {
        val data = repository.findById(principal.name.toInt().toLong())
        return data?.toDomain()
    }
}