package emy.backend.barua.adaptater.provider.gcs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "gcs")
data class GcsProperties(
    var projectId: String = "barua-491306",
    var bucketName: String = "barua_bucket",
    var subdirectory: String = "barua",
    var credentialsLocation: String = "classpath:barua-store.json",
)
