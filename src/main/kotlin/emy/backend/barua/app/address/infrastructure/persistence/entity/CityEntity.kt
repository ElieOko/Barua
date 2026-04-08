package emy.backend.barua.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*

@Table(name = "cities")
class CityEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("province_id")
    val provinceId: Long,
    @Column("name")
    val name: String,
    @Column("is_active")
    var isActive : Boolean = false,
)
