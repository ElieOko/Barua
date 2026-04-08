package emy.backend.barua.app.address.infrastructure.controller

import emy.backend.barua.app.address.application.service.*
import emy.backend.barua.app.address.domain.model.*
import emy.backend.barua.app.address.domain.model.request.*
import emy.backend.barua.route.*
import emy.backend.barua.route.address.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
import io.swagger.v3.oas.annotations.tags.*
import jakarta.servlet.http.*
import jakarta.validation.*
import kotlinx.coroutines.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@Tag(name = "Mairie", description = "Gestion des mairies")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile(Mode.DEV)
class MairieController(
    private val service: MairieService,
    private val cityService: CityService,
    private val sentry: SentryService,
) {
    @PostMapping(MairieScope.PROTECTED, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createMairie(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: MairieRequest,
        @PathVariable version: String,
    ): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val city = cityService.findByIdCity(request.cityId)
            if (city != null) {
                val data = Mairie(
                    city = city.cityId,
                    name = request.name,
                )
                val result = service.saveMairie(data)
                val response = mapOf(
                    "mairie" to result,
                    "message" to "Enregistrement réussie avec succès",
                )
                return ResponseEntity.status(201).body(response)
            }
            val response = mapOf(
                "message" to "cette ville est inexistante !!!",
            )
            return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.mairie.createmairie.count",
                    distributionName = "api.mairie.createmairie.latency",
                ),
            )
        }
    }

    @GetMapping(MairieScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllMairie(
        request: HttpServletRequest,
        @PathVariable version: String,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllMairie()
            val response = mapOf("mairies" to data)
            ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.mairie.getallmairie.count",
                    distributionName = "api.mairie.getallmairie.latency",
                ),
            )
        }
    }
}
