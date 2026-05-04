package emy.backend.barua.app.organism.domain.model

import emy.backend.barua.app.organism.infrastructure.persistance.entities.OrganismEntity

data class Organism(
    val id: Long?,
    val typeId: Long,
    val name: String,
    val description: String? = null,
    val isActive: Boolean,
)



data class OrganismDAO(
    val id: Long?,
    val city : String,
    val type: TypeOrganism?,
    val name: String,
    val description: String? = null,
)
fun Organism.toEntity()= OrganismEntity(
    id = this.id,
    typeId = this.typeId,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)