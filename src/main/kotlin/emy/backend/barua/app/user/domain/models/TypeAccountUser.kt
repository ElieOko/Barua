package emy.backend.barua.app.user.domain.models

import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountUserEntity

data class AccountUser(
    val id: Long? = null,
    val userId: Long,
    val accountId: Long,
    val organismId: Long? = null,
)

fun AccountUser.toEntity() = AccountUserEntity(
    id = this.id,
    accountId = this.accountId,
    userId = this.userId,
    organismId = this.organismId,
)
