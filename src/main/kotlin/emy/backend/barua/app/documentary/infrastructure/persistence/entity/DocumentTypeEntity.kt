package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.DocumentType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*

@Table("document_types")
data class DocumentTypeEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("name")
    val name: String,
    @Column("description")
    val description: String? = null,
    @Column("is_active")
    val isActive: Boolean = true
)
fun DocumentTypeEntity.toDomain() = DocumentType(
    documentTypeId = this.id,
    name = this.name,
    description = this.description,
    isActive = this.isActive,
)