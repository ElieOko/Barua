package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.CountryEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CountryRepository : CoroutineCrudRepository<CountryEntity, Long>