package emy.backend.barua.app.user.domain.models

import org.jetbrains.annotations.NotNull

data class PermissionUser(
    @NotNull
    val organismId: Long,
    @NotNull
    val profileId: Long,
)

data class PermissionUserRequest(
    val roles: List<PermissionUser>,
)
