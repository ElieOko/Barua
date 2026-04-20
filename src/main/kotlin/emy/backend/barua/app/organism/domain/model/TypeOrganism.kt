package emy.backend.barua.app.organism.domain.model

import emy.backend.barua.app.organism.infrastructure.persistance.entities.TypeOrganismEntity

data class TypeOrganism(
    val id: Long?,
    val name: String,
    val description: String? = null,
    val isActive: Boolean,
)

fun TypeOrganism.toEntity()= TypeOrganismEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)