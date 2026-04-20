package emy.backend.barua.app.documentary.infrastructure.controller

import emy.backend.barua.app.documentary.application.service.DemandeProcurationService
import emy.backend.barua.app.documentary.domain.model.DemandeProcuration
import emy.backend.barua.app.documentary.domain.model.request.*
import emy.backend.barua.app.documentary.infrastructure.route.DemandeProcurationScope
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.security.Auth
import emy.backend.barua.security.monitoring.MetricModel
import emy.backend.barua.security.monitoring.SentryService
import emy.backend.barua.utils.Mode
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@Tag(name = "DemandeProcuration", description = "Demandes de procuration de documents")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}/")
@Profile(Mode.DEV)
class DemandeProcurationController(
    private val service: DemandeProcurationService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    @PostMapping(DemandeProcurationScope.PROTECTED, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: DemandeProcurationRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId?:throw ResponseStatusException(HttpStatusCode.valueOf(402), "Utilisateur non authentifié.")
            val data = DemandeProcuration(
                userId = userId,
                serviceDocumentaireId = request.serviceDocumentaireId,
                commentaire = request.commentaire,
            )
            val result = service.save(data)
             ResponseEntity.status(201).body(
                mapOf(
                    "demande" to result,
                    "message" to "Demande enregistrée",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.create.count",
                    distributionName = "api.demandeprocuration.create.latency",
                ),
            )
        }
    }
}
