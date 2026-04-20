package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.DocumentEntity

data class Document(
    var documentId: Long? = null,
    val documentTypeId: Long,
    val title: String,
    val code: String? = null,
    val description: String? = null,
    val isActive: Boolean = true,
)

fun Document.toEntity() = DocumentEntity(
    id = this.documentId,
    documentTypeId = this.documentTypeId,
    title = this.title,
    code = this.code,
    description = this.description,
    isActive = this.isActive,
)