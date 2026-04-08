package emy.backend.barua.app.address.application.service

import emy.backend.barua.app.address.domain.model.Mairie
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toDomain
import emy.backend.barua.app.address.infrastructure.persistence.mapper.toEntity
import emy.backend.barua.app.address.infrastructure.persistence.repository.MairieRepository
import emy.backend.barua.utils.Mode
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile(Mode.DEV)
class MairieService(
    private val repository: MairieRepository,
) {
    suspend fun saveMairie(data: Mairie): Mairie? {
        val entity = data.toEntity()
        val result = repository.save(entity)
        return result.toDomain()
    }

    suspend fun findAllMairie() = repository.findAll().map { it.toDomain() }.toList()

    suspend fun findByIdMairie(id: Long?): Mairie? {
        val data = if (id == null) null else repository.findById(id)
        return data?.toDomain()
    }
}
