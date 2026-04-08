package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.Country
import emy.backend.barua.app.address.infrastructure.persistence.entity.CountryEntity

fun CountryEntity.toDomain() = Country( countryId = this.id, name = this.name)

fun Country.toEntity()  = CountryEntity( id = this.countryId, name = this.name)