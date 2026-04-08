package emy.backend.barua.app.address.infrastructure.controller

import emy.backend.barua.app.address.application.service.*
import emy.backend.barua.app.address.domain.model.*
import emy.backend.barua.app.address.domain.model.request.*
import emy.backend.barua.route.*
import emy.backend.barua.route.address.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
import io.swagger.v3.oas.annotations.tags.*
import jakarta.validation.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.*
import kotlinx.coroutines.*

@Tag(name = "District", description = "Gestion des districts")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile(Mode.DEV)
class DistrictController(
    private val service : DistrictService,
    private val cityService: CityService,
    private val sentry: SentryService,
) {
    @PostMapping(DistrictScope.PROTECTED,consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createDistrict(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: DistrictRequest,
        @PathVariable version: String
    ): ResponseEntity<out Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val city = cityService.findByIdCity(request.cityId)
            if (city != null){
                val data = District(
                    city = city.cityId,
                    name = request.name
                )
              val result = service.saveDistrict(data)
              val response = mapOf(
                  "district" to result,
                  "message" to "Enregistrement réussie avec succès"
              )
                return ResponseEntity.status(201).body(response)}
            val response = mapOf(
                "message" to "cette ville est inexistante !!!"
            )
            return ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.district.createdistrict.count",
                    distributionName = "api.district.createdistrict.latency"
                )
            )
        }
    }

    @GetMapping(DistrictScope.PUBLIC,produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllDistrict(
        request: HttpServletRequest,
        @PathVariable version: String) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllDistrict()
            val response = mapOf("districts" to data)
            ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.district.getalldistrict.count",
                    distributionName = "api.district.getalldistrict.latency"
                )
            )
        }
    }
}