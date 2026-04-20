package emy.backend.barua.app.organism.infrastructure.persistance.entities

import emy.backend.barua.app.organism.domain.model.TypeOrganism
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("type_organism")
class TypeOrganismEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("is_active")
    val isActive: Boolean = true,
)

fun TypeOrganismEntity.toDomain()= TypeOrganism(
    id = this.id,
    name = this.name,
    description = this.description,
    isActive = this.isActive
)