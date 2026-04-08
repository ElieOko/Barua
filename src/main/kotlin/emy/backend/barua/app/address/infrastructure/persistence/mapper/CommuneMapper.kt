package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.Commune
import emy.backend.barua.app.address.infrastructure.persistence.entity.CommuneEntity

fun CommuneEntity.toDomain() = Commune( communeId = this.id, name = this.name)

fun CommuneEntity.toDomainOrigin() = Commune(
    communeId = this.id,
//    quartiers = this.,
    district = this.districtId,
    name = this.name
)

fun Commune.toEntity() = CommuneEntity(
    id = this.communeId,
    districtId = this.district,
    name = this.name
)