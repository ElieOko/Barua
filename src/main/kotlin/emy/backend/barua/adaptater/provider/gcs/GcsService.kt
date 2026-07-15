package emy.backend.barua.adaptater.provider.gcs

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class GcsService(
    private val gcsProperties: GcsProperties,
    resourceLoader: ResourceLoader,
) {
    private val storage: Storage
    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        val credentialsResource = resourceLoader.getResource(gcsProperties.credentialsLocation)
        val credentials = GoogleCredentials.fromStream(credentialsResource.inputStream)
            .createScoped("https://www.googleapis.com/auth/devstorage.read_write")
        storage = StorageOptions.newBuilder()
            .setProjectId(gcsProperties.projectId)
            .setCredentials(credentials)
            .build()
            .service
        log.info(
            "GCS initialisé: project={}, bucket={}, credentials={}",
            gcsProperties.projectId,
            gcsProperties.bucketName,
            gcsProperties.credentialsLocation,
        )
    }

    @Throws(IOException::class)
    fun uploadFile(
        file: MultipartFile,
        directory: String = "",
        fileName: String? = null,
    ): String {
        val resolvedFileName = fileName ?: file.originalFilename
            ?: throw IllegalArgumentException("Le nom du fichier est requis")
        val objectPath = buildObjectPath(directory, resolvedFileName)

        log.info("Upload GCS: bucket={}, path={}", gcsProperties.bucketName, objectPath)

        val blobInfoBuilder = BlobInfo.newBuilder(gcsProperties.bucketName, objectPath)
        file.contentType?.let { blobInfoBuilder.setContentType(it) }

        val blob = storage.create(blobInfoBuilder.build(), file.bytes)
        log.info("Fichier uploadé: {}", blob.mediaLink)
        return blob.mediaLink
    }

    fun deleteFile(fileName: String, directory: String = ""): Boolean {
        return try {
            val objectPath = buildObjectPath(directory, fileName)
            storage.delete(BlobId.of(gcsProperties.bucketName, objectPath))
        } catch (e: Exception) {
            log.warn("Échec suppression GCS: {}", e.message)
            false
        }
    }

    private fun buildObjectPath(directory: String, fileName: String): String {
        val normalizedDirectory = directory.trim('/').let { if (it.isEmpty()) "" else "$it/" }
        return "${gcsProperties.subdirectory}/$normalizedDirectory$fileName"
    }
}
