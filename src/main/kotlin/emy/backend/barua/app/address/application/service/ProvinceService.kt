package emy.backend.barua.app.address.application.service

import emy.backend.barua.app.address.domain.model.Province
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.app.address.infrastructure.persistence.repository.ProvinceRepository
import emy.backend.barua.utils.Mode
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile(Mode.DEV)
class ProvinceService(
    private val repository: ProvinceRepository,
) {
    suspend fun saveProvince(data: Province): Province? {
        val entity = data.toEntity()
        val result = repository.save(entity)
        return result.toDomain()
    }

    suspend fun findAllProvince() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdProvince(id: Long?): Province? {
        val data = if (id == null) null else repository.findById(id)
        return data?.toDomain()
    }
}
