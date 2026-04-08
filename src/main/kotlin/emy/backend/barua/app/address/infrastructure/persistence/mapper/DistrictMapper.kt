package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.District
import emy.backend.barua.app.address.infrastructure.persistence.entity.DistrictEntity

fun District.toEntity() = DistrictEntity(id = this.districtId, cityId = this.city, name = this.name)

fun DistrictEntity.toDomain() = District( districtId = this.id, city = this.cityId, name = this.name)