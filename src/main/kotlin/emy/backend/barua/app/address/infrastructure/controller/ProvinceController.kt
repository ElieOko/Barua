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

@Tag(name = "Province", description = "Gestion des provinces")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile(Mode.DEV)
class ProvinceController(
    private val service: ProvinceService,
    private val countryService: CountryService,
    private val sentry: SentryService,
) {
    @PostMapping(ProvinceScope.PROTECTED, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createProvince(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ProvinceRequest,
        @PathVariable version: String,
    ): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val country = countryService.findByIdCountry(request.countryId)
            if (country != null) {
                val data = Province(
                    country = request.countryId,
                    name = request.name,
                )
                val result = service.saveProvince(data)
                val response = mapOf(
                    "province" to result,
                    "message" to "Enregistrement réussie avec succès",
                )
                return ResponseEntity.status(201).body(response)
            }
            val response = mapOf(
                "message" to "ce pays est inexistant !!!",
            )
            return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.province.createprovince.count",
                    distributionName = "api.province.createprovince.latency",
                ),
            )
        }
    }

    @GetMapping(ProvinceScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllProvince(
        request: HttpServletRequest,
        @PathVariable version: String,
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllProvince()
            val response = mapOf("provinces" to data)
            ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.province.getallprovince.count",
                    distributionName = "api.province.getallprovince.latency",
                ),
            )
        }
    }
}
