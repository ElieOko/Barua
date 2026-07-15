package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.DemandeProcurationStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("demande_procuration_status")
data class DemandeProcurationStatusEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("code")
    val code: String,
    @Column("label")
    val label: String,
    @Column("is_active")
    val isActive: Boolean = true,
)

fun DemandeProcurationStatusEntity.toDomain() = DemandeProcurationStatus(
    statusId = id!!,
    code = code,
    label = label,
    isActive = isActive,
)
