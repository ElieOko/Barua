package emy.backend.barua.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import emy.backend.barua.app.address.application.service.*
import emy.backend.barua.app.address.domain.model.*
import emy.backend.barua.route.*
import emy.backend.barua.route.address.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
import jakarta.servlet.http.*

@Tag(name = "City", description = "Gestion des villes")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile(Mode.DEV)
class CityController(
    private val service : CityService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste de villes")
    @GetMapping(CityScope.PUBLIC,produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCity(request: HttpServletRequest, @PathVariable version: String): ResponseEntity<Map<String, List<City?>>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllCity()
            val response = mapOf("cities" to data)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.city.getallcity.count",
                    distributionName = "api.city.getallcity.latency"
                )
            )
        }
    }
}