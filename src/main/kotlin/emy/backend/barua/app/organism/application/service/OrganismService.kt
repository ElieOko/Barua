package emy.backend.barua.app.organism.application.service

import emy.backend.barua.app.organism.domain.model.OrganismDAO
import emy.backend.barua.app.organism.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.organism.infrastructure.persistance.repository.*
import emy.backend.barua.app.user.application.services.AccountService
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class OrganismService(
    private val repository: OrganismRepository,
    private val typeOrganism: TypeOrganismRepository,
    private val city: CityRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun findAll() = coroutineScope {
        val items = mutableListOf<OrganismDAO>()
        repository.findAll().collect {
            val c = (city.findById(it.cityId?:0L))?.name?:""
            items.add(
                OrganismDAO(
                    id = it.id,
                    type = typeOrganism.findById(it.typeId)!!.toDomain(),
                    name = it.name,
                    city = c,
                    description = it.description,
                )
            )
        }
        items
    }
    suspend fun findById(id: Long) = coroutineScope {
        repository.findById(id)?.toDomain()?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Organism Not Found with ID $id."
        )
    }
}
