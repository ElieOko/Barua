package emy.backend.barua.config

import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.reactive.config.*

@Configuration
class WebConfiguration : WebFluxConfigurer {

    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .addSupportedVersions("1.0","2.0")
//            .useRequestHeader("API-Version")
            .useMediaTypeParameter(MediaType.APPLICATION_JSON, "version")
            .setDefaultVersion("1.0")
            .setVersionParser(ApiVersionParser())

    }

}


