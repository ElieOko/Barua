package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.RefreshToken
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RefreshTokenRepository : CoroutineCrudRepository<RefreshToken, Long> {
    suspend fun findByUserIdAndHashedToken(userId: Long, hashedToken: String): RefreshToken?
    suspend fun deleteByUserIdAndHashedToken(userId: Long, hashedToken: String)
}