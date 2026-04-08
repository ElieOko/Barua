package emy.backend.barua.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("districts")
data class DistrictEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("city_id")
    val cityId: Long?,
    @Column("name")
    val name: String,
    @Column("is_active")
    var isActive : Boolean = false,
)
