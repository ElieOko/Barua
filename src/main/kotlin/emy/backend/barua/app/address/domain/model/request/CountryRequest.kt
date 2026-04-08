package emy.backend.barua.app.address.domain.model.request


import jakarta.validation.constraints.NotNull

data class CountryRequest(
    @NotNull
    val name : String
)
