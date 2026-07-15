package emy.backend.barua.app.organism.infrastructure.persistance.entities

import emy.backend.barua.app.organism.domain.model.Organism
import emy.backend.barua.app.organism.domain.model.TypeOrganism
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("organisms")
class OrganismEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("type_id")
    val typeId: Long,
    @Column("city_id")
    val cityId: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("is_active")
    val isActive: Boolean = true,
)

fun OrganismEntity.toDomain()= Organism(
    id = this.id,
    typeId = this.typeId,
    name = this.name,
    description = this.description,
    cityId = this.cityId,
    isActive = this.isActive
)