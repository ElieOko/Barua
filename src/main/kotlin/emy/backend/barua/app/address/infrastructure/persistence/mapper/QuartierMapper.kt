package emy.backend.barua.app.address.infrastructure.persistence.mapper

import emy.backend.barua.app.address.domain.model.Quartier
import emy.backend.barua.app.address.infrastructure.persistence.entity.QuartierEntity

fun QuartierEntity.toDomain() = Quartier(quartierId = this.id, name = this.name)

fun Quartier.toEntity()  = QuartierEntity(id = this.quartierId, name = this.name)
