package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.Province
import emy.backend.barua.app.address.infrastructure.persistence.entity.ProvinceEntity

fun Province.toEntity() = ProvinceEntity(
    id = this.provinceId,
    name = this.name,
    countryId = this.country,
)

fun ProvinceEntity.toDomain() = Province(
    provinceId = this.id,
    country = this.countryId,
    name = this.name,
)
