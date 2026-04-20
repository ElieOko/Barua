package emy.backend.barua.app.organism.infrastructure.persistance.repository

import emy.backend.barua.app.organism.infrastructure.persistance.entities.TypeOrganismEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TypeOrganismRepository : CoroutineCrudRepository<TypeOrganismEntity, Long>