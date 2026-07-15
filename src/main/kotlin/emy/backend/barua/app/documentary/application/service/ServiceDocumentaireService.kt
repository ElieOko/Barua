package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaire
import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaireDTO
import emy.backend.barua.app.documentary.domain.model.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.ServiceDocumentaireRepository
import emy.backend.barua.app.organism.application.service.OrganismService
import emy.backend.barua.app.organism.domain.model.OrganismDAO
import emy.backend.barua.app.organism.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.organism.infrastructure.persistance.repository.CityRepository
import emy.backend.barua.app.organism.infrastructure.persistance.repository.TypeOrganismRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ServiceDocumentaireService(
    private val repository: ServiceDocumentaireRepository,
    private val documentService: DocumentService,
    private val organismService: OrganismService,
    private val typeOrganism: TypeOrganismRepository,
    private val city: CityRepository,
) {
    suspend fun save(data: ServiceDocumentaire): ServiceDocumentaire? {
        documentService.findById(data.documentId)
        organismService.findById(data.organismId)
        val saved = repository.save(data.toEntity())
        return saved.toDomain()
    }

    suspend fun findAll() = coroutineScope {
        val items = mutableListOf<ServiceDocumentaireDTO>()
        repository.findAll().collect {
            val org = organismService.findById(it.organismId)
            val c = (city.findById(org.cityId?:0L))?.name?:""
            items.add(ServiceDocumentaireDTO(
                serviceDocumentaireId = it.serviceDocumentaireId,
                document = documentService.findById(it.documentId),
                organism = OrganismDAO(
                    id = org.id,
                    type = typeOrganism.findById(org.typeId)!!.toDomain(),
                    name = org.name,
                    city = c,
                    description = it.description,
                ),
                price = it.price,
                description = it.description,
                deviseId = it.deviseId,
                delayDayOpen = it.delayDayOpen))
        }
        items.toList()
    }

    suspend fun findByDocumentId(documentId: Long) =
        repository.findByDocumentId(documentId).map { it.toDomain() }.toList()

    suspend fun findById(id: Long) = coroutineScope {
        repository.findById(id)?.toDomain()?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Document Not Found with ID $id."
        )
    }
    suspend fun detail(id: Long) = coroutineScope {
        val data = repository.findById(id)?.toDomain()?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Document Not Found with ID $id."
        )
        val org = organismService.findById(data.organismId)
        val c = (city.findById(org.cityId?:0L))?.name?:""
        val items = ServiceDocumentaireDTO(
            serviceDocumentaireId = data.serviceDocumentaireId,
            document = documentService.findById(data.documentId),
            organism = OrganismDAO(
                id = org.id,
                type = typeOrganism.findById(org.typeId)!!.toDomain(),
                name = org.name,
                city = c,
                description = data.description,
            ),
            price = data.price,
            description = data.description,
            deviseId = data.deviseId,
            delayDayOpen = data.delayDayOpen)
        items
    }

    suspend fun update(id: Long, data: ServiceDocumentaire): ServiceDocumentaire? {
        if (repository.findById(id) == null) return null
        documentService.findById(data.documentId)
        organismService.findById(data.organismId)
        val updated = data.copy(serviceDocumentaireId = id)
        return repository.save(updated.toEntity()).toDomain()
    }
}
