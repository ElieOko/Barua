package emy.backend.barua.app.documentary.application.service

import emy.backend.barua.adaptater.provider.gcs.GcsService
import emy.backend.barua.app.documentary.domain.model.DemandeProcurationPiece
import emy.backend.barua.app.documentary.domain.model.toEntity
import emy.backend.barua.app.documentary.infrastructure.persistence.entity.toDomain
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.DemandeProcurationPieceRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class DemandeProcurationPieceService(
    private val repository: DemandeProcurationPieceRepository,
    private val gcsService: GcsService,
) {
    private val subdirectory = "demande-procuration/"

    suspend fun saveAll(demandeProcurationId: Long, files: List<MultipartFile>): List<DemandeProcurationPiece> {
        if (files.isEmpty()) return emptyList()

        return files.map { file ->
            val originalName = file.originalFilename ?: file.name
            val fileNameSaved = "${System.currentTimeMillis()}_${demandeProcurationId}_$originalName"
            val path = gcsService.uploadFile(file, subdirectory, fileNameSaved)
            val saved = repository.save(
                DemandeProcurationPiece(
                    demandeProcurationId = demandeProcurationId,
                    name = originalName,
                    path = path,
                    contentType = file.contentType,
                ).toEntity(),
            )
            saved.toDomain()
        }
    }

    suspend fun findByDemandeProcurationIdIn(demandeProcurationIds: List<Long>) =
        repository.findByDemandeProcurationIdIn(demandeProcurationIds)
            .toList()
            .map { it.toDomain() }
}
