package emy.backend.barua.app.address.infrastructure.controller

import emy.backend.barua.app.address.application.service.*
import emy.backend.barua.app.address.domain.model.Commune
import emy.backend.barua.app.address.domain.model.request.CommuneRequest
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.Mode
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest

@Tag(name = "Commune", description = "Gestion des communes")
@RestController
@RequestMapping("api")
@Profile(Mode.DEV)
class CommuneController(
    private val service : CommuneService,
    private val districtService: DistrictService,
    private val sentry: SentryService,
) {
    @PostMapping("/{version}/${CommuneScope.PROTECTED}",consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun createCommune(
       httpRequest: HttpServletRequest,
       @Valid @RequestBody request: CommuneRequest
    ): ResponseEntity<Map<String, Any>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val district = districtService.findByIdDistrict(request.districtId)
            if (district != null){
                val data = Commune(
                    district = district.districtId,
                    name = request.name
                )
              val result = service.saveCommune(data)
              val response = mapOf(
                  "commune" to result as Commune,
                  "message" to "Enregistrement réussie avec succès"
              )
                ResponseEntity.status(201).body(response)
            }
            val response = mapOf(
                "message" to "cet district est inexistant !!!"
            )
            ResponseEntity.badRequest().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.commune.createcommune.count",
                    distributionName = "api.commune.createcommune.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/${CommuneScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllCommune(request: HttpServletRequest): ResponseEntity<Map<String, List<Commune>>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = service.findAllCommune()
            val response = mapOf("communes" to data)
            ResponseEntity.ok().body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.commune.getallcommune.count",
                    distributionName = "api.commune.getallcommune.latency"
                )
            )
        }
    }
}