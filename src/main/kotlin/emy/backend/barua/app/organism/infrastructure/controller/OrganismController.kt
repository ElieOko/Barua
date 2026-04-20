package emy.backend.barua.app.organism.infrastructure.controller

import emy.backend.barua.app.organism.application.service.OrganismService
import emy.backend.barua.app.organism.infrastructure.route.OrganismScope
import emy.backend.barua.app.organism.infrastructure.route.OrganismTypeScope
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.Mode
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Organism", description = "Registre unifié : communes, mairies, ministères, services publics")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}/")
@Profile(Mode.DEV)
class OrganismController(
    private val service: OrganismService,
    private val sentry: SentryService,
) {
    @GetMapping(OrganismScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll(
        request: HttpServletRequest,
        @PathVariable version: String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok(mapOf("organism" to service.findAll()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.organism.getall.count",
                    distributionName = "api.organism.getall.latency",
                ),
            )
        }
    }
}
