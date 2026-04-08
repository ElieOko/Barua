package emy.backend.barua.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*

@Table(name = "provinces")
class ProvinceEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("country_id")
    val countryId: Long,
    @Column("is_active")
    var isActive : Boolean = false,
)
