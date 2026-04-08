package emy.backend.barua.app.address.infrastructure.controller

import io.swagger.v3.oas.annotations.tags.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import emy.backend.barua.app.address.application.service.*
import emy.backend.barua.app.address.domain.model.*
import emy.backend.barua.utils.Mode
import emy.backend.barua.security.monitoring.*
import jakarta.servlet.http.HttpServletRequest

@Tag(name = "Quartier", description = "Gestion des quartiers")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class QuartierController(
    private val service : QuartierService,
    private val sentry: SentryService,
) {
    @GetMapping("/{version}/${QuartierScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllQuartier(request: HttpServletRequest): ResponseEntity<Map<String, List<Quartier>>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllQuartier()
            val response = mapOf("quartiers" to data)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.quartier.getallquartier.count",
                    distributionName = "api.quartier.getallquartier.latency"
                )
            )
        }
    }
}