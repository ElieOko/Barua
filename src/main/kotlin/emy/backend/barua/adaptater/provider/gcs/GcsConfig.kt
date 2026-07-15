package emy.backend.barua.adaptater.provider.gcs

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GcsProperties::class)
class GcsConfig
