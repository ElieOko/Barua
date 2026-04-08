package emy.backend.barua.app.address.domain.model.request

import jakarta.validation.constraints.NotNull

data class ProvinceRequest(
    @NotNull
    val name: String,
    @NotNull
    val countryId: Long,
)
