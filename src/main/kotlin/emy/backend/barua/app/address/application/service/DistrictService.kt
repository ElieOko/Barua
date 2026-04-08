package emy.backend.barua.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import emy.backend.barua.app.address.domain.model.District
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.app.address.infrastructure.persistence.repository.DistrictRepository

@Service
class DistrictService(
    private val repository: DistrictRepository
) {
    suspend fun saveDistrict(data: District): District? {
        val entity = data.toEntity()
        val result = repository.save(entity)
        return result.toDomain()

    }
    suspend fun findAllDistrict() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdDistrict(id : Long) = repository.findById(id)?.toDomain()
}