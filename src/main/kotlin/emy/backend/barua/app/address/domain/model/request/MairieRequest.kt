package emy.backend.barua.app.address.domain.model.request

import jakarta.validation.constraints.NotNull

data class MairieRequest(
    @NotNull
    val name: String,
    @NotNull
    val cityId: Long,
)
