package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DocumentTypeEntity

data class DocumentType(
    var documentTypeId: Long? = null,
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
)
fun DocumentType.toEntity() = DocumentTypeEntity(
    id = this.documentTypeId,
    name = this.name,
    description = this.description,
    isActive = this.isActive,
)