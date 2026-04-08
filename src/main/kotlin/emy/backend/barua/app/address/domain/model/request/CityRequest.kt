package emy.backend.barua.app.address.domain.model.request

import jakarta.validation.constraints.NotNull

data class CityRequest(
    @NotNull
    val countryId : Long,
    @NotNull
    val name : String,
)