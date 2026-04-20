package emy.backend.barua.app.organism.domain.model

import emy.backend.barua.app.organism.infrastructure.persistance.entities.OrganismEntity

data class Organism(
    val id: Long?,
    val typeId: Long,
    val name: String,
    val description: String? = null,
    val isActive: Boolean,
)

fun Organism.toEntity()= OrganismEntity(
    id = this.id,
    typeId = this.typeId,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)