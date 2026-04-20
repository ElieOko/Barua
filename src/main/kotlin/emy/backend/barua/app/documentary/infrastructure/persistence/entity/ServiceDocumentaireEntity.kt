package emy.backend.barua.app.documentary.infrastructure.persistence.entity

import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaire
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.*
import java.time.LocalDateTime

@Table("service_documentaries")
data class ServiceDocumentaireEntity(
    @Id
    @Column("id")
    var serviceDocumentaireId: Long? = null,
    @Column("document_id")
    val documentId: Long,
    @Column("organism_id")
    val organismId: Long,
    @Column("user_id")
    val userId: Long,
    @Column("price")
    val price: Double,
    @Column("devise_id")
    val deviseId: Long,
    @Column("description")
    val description: String? = "",
    @Column("delay_day_open")
    val delayDayOpen: String,
    @Column("is_active")
    val isActive: Boolean = true,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

fun ServiceDocumentaireEntity.toDomain()=  ServiceDocumentaire(
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