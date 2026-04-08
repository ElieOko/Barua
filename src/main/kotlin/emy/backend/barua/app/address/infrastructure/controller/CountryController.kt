package emy.backend.barua.app.address.infrastructure.controller

import emy.backend.barua.app.address.application.service.*
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import emy.backend.barua.app.address.domain.model.*
import emy.backend.barua.route.*
import emy.backend.barua.route.address.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
import jakarta.servlet.http.*

@Tag(name = "Country", description = "Gestion des pays")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile(Mode.DEV)
class CountryController(
    private val service : CountryService,
    private val sentry: SentryService,
) {
    @GetMapping(CountryScope.PUBLIC,produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCountry(request: HttpServletRequest, @PathVariable version: String): ResponseEntity<Map<String, List<Country>>> {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllCountry()
            val response = mapOf("countries" to data)
            return ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.country.getallcountry.count",
                    distributionName = "api.country.getallcountry.latency"
                )
            )
        }
    }
}