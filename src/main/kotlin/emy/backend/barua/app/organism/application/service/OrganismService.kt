package emy.backend.barua.app.organism.application.service

import emy.backend.barua.app.organism.domain.model.Organism
import emy.backend.barua.app.organism.infrastructure.persistance.repository.OrganismRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class OrganismService(
    private val repository: OrganismRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun findAll() = coroutineScope { repository.findAll().toList() }
    suspend fun findById(id: Long) = coroutineScope {
        repository.findById(id)?:throw ResponseStatusException(
            HttpStatusCode.valueOf(404),
            "Organism Not Found with ID $id."
        )
    }
}
