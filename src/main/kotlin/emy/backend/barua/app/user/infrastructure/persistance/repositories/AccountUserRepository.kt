package emy.backend.barua.app.user.infrastructure.persistance.repositories

import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountUserEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AccountUserRepository : CoroutineCrudRepository<AccountUserEntity, Long> {
    @Query("SELECT * FROM account_users WHERE user_id = :userId AND account_id = :accountId")
    suspend fun findByUserAndAccount(userId: Long, accountId : Long) : AccountUserEntity?
    @Query(
        """
        SELECT * FROM account_users
        WHERE user_id = :userId
          AND account_id = :accountId
          AND (
              (:organismId IS NULL AND organism_id IS NULL)
              OR organism_id = :organismId
          )
        LIMIT 1
        """,
    )
    suspend fun findByUserIdAndAccountIdAndOrganismId(
        userId: Long,
        accountId: Long,
        organismId: Long?,
    ): AccountUserEntity?
    fun findByUserIdIn(userIds: List<Long>): Flow<AccountUserEntity>
    @Query("SELECT * FROM account_users WHERE user_id = :userId")
    fun findAllAccountByUserId(userId: Long): Flow<AccountUserEntity>
}