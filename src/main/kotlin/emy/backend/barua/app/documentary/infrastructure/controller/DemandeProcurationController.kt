package emy.backend.barua.app.documentary.infrastructure.controller

import emy.backend.barua.app.documentary.application.service.DemandeProcurationHistoriqueService
import emy.backend.barua.app.documentary.application.service.DemandeProcurationService
import emy.backend.barua.app.documentary.application.service.DemandeProcurationStatusService
import emy.backend.barua.app.documentary.domain.model.DemandeProcuration
import emy.backend.barua.app.documentary.domain.model.DemandeProcurationStatusCodes
import emy.backend.barua.app.documentary.domain.model.request.*
import emy.backend.barua.app.documentary.infrastructure.ensureDocumentaryAdmin
import emy.backend.barua.app.documentary.infrastructure.route.DemandeProcurationScope
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.security.Auth
import emy.backend.barua.security.monitoring.MetricModel
import emy.backend.barua.security.monitoring.SentryService
import emy.backend.barua.utils.Mode
import emy.backend.barua.utils.bufferMultipartFile
import io.swagger.v3.oas.annotations.Operation
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
    private val statusService: DemandeProcurationStatusService,
    private val historiqueService: DemandeProcurationHistoriqueService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    @Operation(summary = "Lister les statuts disponibles")
    @GetMapping("${DemandeProcurationScope.PUBLIC}/status", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun listStatuses(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok(mapOf("status" to statusService.findAllActive()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.liststatus.count",
                    distributionName = "api.demandeprocuration.liststatus.latency",
                ),
            )
        }
    }

    @Operation(summary = "Créer une demande de procuration avec pièces jointes")
    @PostMapping(DemandeProcurationScope.PROTECTED, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @ModelAttribute request: DemandeProcurationRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId
                ?: throw ResponseStatusException(HttpStatusCode.valueOf(402), "Utilisateur non authentifié.")
            val initialStatus = statusService.findByCode(DemandeProcurationStatusCodes.SOUMISES)
            val bufferedPieces = request.piece.orEmpty()
                .filter { !it.isEmpty }
                .map(::bufferMultipartFile)
            val data = DemandeProcuration(
                userId = userId,
                serviceDocumentaireId = request.serviceDocumentaireId!!,
                statusId = initialStatus.statusId,
                commentaire = request.commentaire,
                numberIdentity = request.numberIdentity,
                phone = request.phone,
                fullName = request.fullName,
            )
            val result = service.save(data, bufferedPieces)
            ResponseEntity.status(201).body(
                mapOf(
                    "demande" to result,
                    "message" to "Demande enregistrée avec ${bufferedPieces.size} pièce(s)",
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

    @GetMapping(DemandeProcurationScope.PROTECTED, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun listByUser(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId
                ?: throw ResponseStatusException(HttpStatusCode.valueOf(402), "Utilisateur non authentifié.")
            ResponseEntity.ok(mapOf("demandes" to service.findByUserId(userId)))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.listbyuser.count",
                    distributionName = "api.demandeprocuration.listbyuser.latency",
                ),
            )
        }
    }

    @Operation(summary = "Voir l'évolution du statut d'une demande")
    @GetMapping("${DemandeProcurationScope.PROTECTED}/{id}/evolution", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun evolutionByUser(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId
                ?: throw ResponseStatusException(HttpStatusCode.valueOf(402), "Utilisateur non authentifié.")
            service.ensureOwnedByUser(id, userId)
            ResponseEntity.ok(mapOf("historique" to historiqueService.findByDemandeIdDto(id)))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.evolutionbyuser.count",
                    distributionName = "api.demandeprocuration.evolutionbyuser.latency",
                ),
            )
        }
    }

    @GetMapping(DemandeProcurationScope.PRIVATE_ADMIN, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun listAll(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)
            ResponseEntity.ok(mapOf("demandes" to service.findAll()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.listall.count",
                    distributionName = "api.demandeprocuration.listall.latency",
                ),
            )
        }
    }

    @Operation(summary = "Changer le statut d'une demande")
    @PutMapping("${DemandeProcurationScope.PRIVATE_ADMIN}/{id}/status", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun changeStatus(
        httpRequest: HttpServletRequest,
        @PathVariable id: Long,
        @PathVariable version: String,
        @Valid @RequestBody request: DemandeProcurationStatusChangeRequest,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)
            val adminUserId = auth.user()?.first?.userId
            val result = service.changeStatus(
                demandeId = id,
                statusCode = request.statusCode,
                note = request.note,
                changedByUserId = adminUserId,
            )
            ResponseEntity.ok(
                mapOf(
                    "demande" to result,
                    "message" to "Statut mis à jour",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.changestatus.count",
                    distributionName = "api.demandeprocuration.changestatus.latency",
                ),
            )
        }
    }

    @Operation(summary = "Lister l'historique des changements de statut")
    @GetMapping("${DemandeProcurationScope.PRIVATE_ADMIN}/historique", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun listHistorique(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)
            ResponseEntity.ok(mapOf("historique" to historiqueService.findAllDto()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.demandeprocuration.listhistorique.count",
                    distributionName = "api.demandeprocuration.listhistorique.latency",
                ),
            )
        }
    }
}
