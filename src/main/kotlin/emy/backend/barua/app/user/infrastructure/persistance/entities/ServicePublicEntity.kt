package emy.backend.barua.app.user.infrastructure.persistance.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "service_publics")
class ServicePublicEntity(
    @Id @Column("id")
    val servicePublicId : Long,
    @Column("nom")
    val nom : String,
    @Column("description")
    val description : String? = null,
    @Column("isActive")
    val isActive : Boolean = true,
)