package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.Document
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("documents")
data class DocumentEntity(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("document_type_id")
    val documentTypeId: Long,
    @Column("title")
    val title: String,
    @Column("code")
    val code: String? = null,
    @Column("description")
    val description: String? = null,
    @Column("is_active")
    val isActive: Boolean = true,
)

fun DocumentEntity.toDomain() = Document(
    documentId = this.id,
    documentTypeId = this.documentTypeId,
    title = this.title,
    code = this.code,
    description = this.description,
    isActive = this.isActive,
)