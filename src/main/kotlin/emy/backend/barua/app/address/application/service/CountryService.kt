package emy.backend.barua.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import emy.backend.barua.app.address.domain.model.Country
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.app.address.infrastructure.persistence.repository.CountryRepository
import emy.backend.barua.utils.Mode

@Service
@Profile(Mode.DEV)
class CountryService(
    private val repository: CountryRepository,
) {
    suspend fun saveCountry(data: Country): Country {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllCountry() = repository.findAll().map { it.toDomain() }.toList()
}