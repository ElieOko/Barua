package emy.backend.barua.app.documentary.infrastructure.controller

import com.google.api.client.util.Data.mapOf
import emy.backend.barua.app.documentary.application.service.ServiceDocumentaireService
import emy.backend.barua.app.documentary.domain.model.ServiceDocumentaire
import emy.backend.barua.app.documentary.domain.model.request.ServiceDocumentaireRequest
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.app.documentary.infrastructure.ensureDocumentaryAdmin
import emy.backend.barua.app.documentary.infrastructure.route.ServiceDocumentaireScope
import emy.backend.barua.security.Auth
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.Mode
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import kotlinx.coroutines.coroutineScope
import org.springframework.context.annotation.Profile
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import kotlin.collections.mapOf

@Tag(name = "ServiceDocumentaire", description = "Tarification et délai par organisme pour un document")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}/")
@Profile(Mode.DEV)
class ServiceDocumentaireController(
    private val service: ServiceDocumentaireService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    @PostMapping(ServiceDocumentaireScope.PRIVATE_ADMIN, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: ServiceDocumentaireRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)?.let { return it }
            val userId = auth.user()?.first?.userId!!
            val data = ServiceDocumentaire(
                documentId = request.documentId,
                price = request.price,
                deviseId = request.devise,
                delayDayOpen = request.delayDayOpen,
                isActive = request.isActive,
                serviceDocumentaireId = null,
                organismId = request.organismId,
                userId = userId,
                description = request.description,
            )
            val result = service.save(data) ?: return ResponseEntity.badRequest().body(
                mapOf(
                    "message" to "Document ou organisme émetteur invalide, ou combinaison déjà existante refusée par la base.",
                ),
            )
            return ResponseEntity.status(201).body(
                mapOf(
                    "serviceDocumentaire" to result,
                    "message" to "Enregistrement réussi",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.servicedocumentaire.create.count",
                    distributionName = "api.servicedocumentaire.create.latency",
                ),
            )
        }
    }

    @PutMapping("${ServiceDocumentaireScope.PRIVATE_ADMIN}/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun update(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
        @PathVariable id: Long,
        @Valid @RequestBody request: ServiceDocumentaireRequest,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)
            val userId = auth.user()?.first?.userId!!
            val data = ServiceDocumentaire(
                documentId = request.documentId,
                price = request.price,
                deviseId = request.devise,
                delayDayOpen = request.delayDayOpen,
                isActive = request.isActive,
                serviceDocumentaireId = null,
                organismId = request.organismId,
                userId = userId,
                description = request.description,
            )
            val result = service.update(id, data) ?: ResponseEntity.badRequest().body(
                mapOf(
                    "message" to "Service documentaire introuvable, ou document / organisme invalide, ou contrainte d'unicité violée.",
                ),
            )
            ResponseEntity.ok(
                mapOf(
                    "serviceDocumentaire" to result,
                    "message" to "Modification réussie",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.servicedocumentaire.update.count",
                    distributionName = "api.servicedocumentaire.update.latency",
                ),
            )
        }
    }

    @GetMapping(ServiceDocumentaireScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll(
        request: HttpServletRequest,
        @PathVariable version: String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok(mapOf("serviceDocumentaries" to service.findAll()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.servicedocumentaire.getall.count",
                    distributionName = "api.servicedocumentaire.getall.latency",
                ),
            )
        }
    }
}
