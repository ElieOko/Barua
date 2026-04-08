package emy.backend.barua.app.address.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class City(
    var cityId : Long?,
    @JsonIgnore
    val province : Long,
    val name : String,
)
