package emy.backend.barua.app.address.application.service

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import emy.backend.barua.app.address.domain.model.Commune
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomainOrigin
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.app.address.infrastructure.persistence.repository.CommuneRepository

@Service
class CommuneService(
    private val repository: CommuneRepository
) {
    suspend fun saveCommune(data: Commune): Commune? {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun findAllCommune() = repository.findAll().map { it.toDomainOrigin() }.toList()

    suspend fun findByIdCommune(id: Long?): Commune? {
        val data = if (id != null) repository.findById(id) else null
       return data?.toDomain()
    }
}