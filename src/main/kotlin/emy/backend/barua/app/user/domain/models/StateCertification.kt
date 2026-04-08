package emy.backend.barua.app.user.domain.models

import org.jetbrains.annotations.*

data class StateCertification(
    @NotNull
    val userId : Long,
    @NotNull
    val isCertified : Boolean
)
