package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.Mairie
import emy.backend.barua.app.address.infrastructure.persistence.entity.MairieEntity

fun Mairie.toEntity() = MairieEntity(
    id = this.mairieId,
    cityId = this.city,
    name = this.name,
)

fun MairieEntity.toDomain() = Mairie(
    mairieId = this.id,
    city = this.cityId,
    name = this.name,
)
