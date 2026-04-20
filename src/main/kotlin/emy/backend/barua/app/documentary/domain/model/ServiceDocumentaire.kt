package emy.backend.barua.app.documentary.domain.model

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.ServiceDocumentaireEntity

data class ServiceDocumentaire(
    var serviceDocumentaireId: Long? = null,
    val documentId: Long,
    val organismId: Long,
    val userId: Long,
    val price: Double,
    val deviseId: Long,
    val description: String? = null,
    val delayDayOpen: String,
    val isActive: Boolean = true
)
fun ServiceDocumentaire.toEntity()=  ServiceDocumentaireEntity(
    serviceDocumentaireId = this.serviceDocumentaireId,
    documentId = this.documentId,
    organismId = this.organismId,
    userId = this.userId,
    price = this.price,
    deviseId = this.deviseId,
    description =this.description,
    delayDayOpen = this.delayDayOpen,
    isActive = this.isActive
)