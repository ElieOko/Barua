package emy.backend.barua.app.user.domain.models.request

import jakarta.validation.constraints.NotNull

data class AssignAccountUserRequest(
    @field:NotNull(message = "L'utilisateur est obligatoire")
    val userId: Long,
    @field:NotNull(message = "Le compte (rôle) est obligatoire")
    val accountId: Long,
    val organismId: Long? = null,
)
