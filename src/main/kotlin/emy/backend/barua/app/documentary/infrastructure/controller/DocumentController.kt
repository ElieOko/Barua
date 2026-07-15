package emy.backend.barua.app.documentary.infrastructure.controller

import emy.backend.barua.app.documentary.application.service.*
import emy.backend.barua.app.documentary.domain.model.Document
import emy.backend.barua.app.documentary.domain.model.request.DocumentRequest
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.app.documentary.infrastructure.ensureDocumentaryAdmin
import emy.backend.barua.app.documentary.infrastructure.route.DocumentScope
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

@Tag(name = "Document", description = "Documents administratifs")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}/")
@Profile(Mode.DEV)
class DocumentController(
    private val service: DocumentService,
    private val documentTypeService: DocumentTypeService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    @PostMapping(DocumentScope.PRIVATE_ADMIN, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: DocumentRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            ensureDocumentaryAdmin(auth)
            documentTypeService.findById(request.documentTypeId)?: return ResponseEntity.badRequest().body(mapOf("message" to "Ce type de document est inexistant."))

            val data = Document(
                documentTypeId = request.documentTypeId,
                title = request.title,
                code = request.code,
                description = request.description,
                isActive = request.isActive,
            )
            val result = service.save(data)
            return ResponseEntity.status(201).body(
                mapOf(
                    "document" to result,
                    "message" to "Enregistrement réussi",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.document.create.count",
                    distributionName = "api.document.create.latency",
                ),
            )
        }
    }

    @PutMapping("${DocumentScope.PRIVATE_ADMIN}/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun update(
        httpRequest: HttpServletRequest,
        @PathVariable version: String,
        @PathVariable id: Long,
        @Valid @RequestBody request: DocumentRequest,
    ): Any {
        val startNanos = System.nanoTime()
        try {
            val userConnect = ensureDocumentaryAdmin(auth)
            if (documentTypeService.findById(request.documentTypeId) == null) {
                return ResponseEntity.badRequest().body(mapOf("message" to "Ce type de document est inexistant."))
            }
            val data = Document(
                documentTypeId = request.documentTypeId,
                title = request.title,
                code = request.code,
                description = request.description,
                isActive = request.isActive,
            )
            val result = service.update(id, data) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("message" to "Document introuvable."))
            return ResponseEntity.ok(
                mapOf(
                    "document" to result,
                    "message" to "Modification réussie",
                ),
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.document.update.count",
                    distributionName = "api.document.update.latency",
                ),
            )
        }
    }

    @GetMapping(DocumentScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll(
        request: HttpServletRequest,
        @PathVariable version: String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok(mapOf("documents" to service.findAll()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.document.getall.count",
                    distributionName = "api.document.getall.latency",
                ),
            )
        }
    }
}
