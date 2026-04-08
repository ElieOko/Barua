package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.City
import emy.backend.barua.app.address.infrastructure.persistence.entity.CityEntity

fun City.toEntity() = CityEntity(id = this.cityId, provinceId = this.province, name = this.name)

fun CityEntity.toDomain() = City(cityId = this.id, province = this.provinceId, name = this.name)
