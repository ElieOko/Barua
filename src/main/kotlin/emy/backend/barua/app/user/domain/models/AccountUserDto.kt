package emy.backend.barua.app.user.domain.models

data class AccountUserDto(
    val id: Long?,
    val userId: Long,
    val accountId: Long,
    val organismId: Long? = null,
)

fun AccountUser.toDto() = AccountUserDto(
    id = id,
    userId = userId,
    accountId = accountId,
    organismId = organismId,
)
