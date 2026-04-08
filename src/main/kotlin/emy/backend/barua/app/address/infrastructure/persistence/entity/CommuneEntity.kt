package emy.backend.barua.app.address.infrastructure.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "communes")
class CommuneEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("district_id")
    val districtId: Long? = null,
    @Column("name")
    val name: String,
    @Column("is_active")
    var isActive : Boolean = false,
)
