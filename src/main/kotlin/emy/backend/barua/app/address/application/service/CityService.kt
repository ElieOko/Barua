package emy.backend.barua.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import emy.backend.barua.app.address.domain.model.City
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.utils.Mode
import emy.backend.barua.app.address.infrastructure.persistence.repository.CityRepository

@Service
@Profile(Mode.DEV)
class CityService(
    private val repository: CityRepository
) {
    suspend fun saveCity(data: City): City? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllCity() : List<City?> = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdCity(id: Long?) : City?  {
        val data = if (id == null) null else repository.findById(id)
        return data?.toDomain()
    }
}