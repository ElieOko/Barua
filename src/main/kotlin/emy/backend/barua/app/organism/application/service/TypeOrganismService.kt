package emy.backend.barua.app.organism.application.service

import emy.backend.barua.app.organism.infrastructure.persistance.repository.TypeOrganismRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TypeOrganismService(
    private val repository: TypeOrganismRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    suspend fun findAll() = coroutineScope { repository.findAll().toList() }
}
