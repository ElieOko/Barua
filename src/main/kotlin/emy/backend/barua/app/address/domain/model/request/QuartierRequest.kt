package emy.backend.barua.app.address.domain.model.request

import jakarta.validation.constraints.NotNull

data class QuartierRequest(
    @NotNull
    val name : String,
    @NotNull
    val communeId : Long
)

