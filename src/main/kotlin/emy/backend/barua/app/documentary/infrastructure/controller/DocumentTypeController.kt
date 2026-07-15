package emy.backend.barua.app.documentary.infrastructure.controller

import emy.backend.barua.app.documentary.application.service.DocumentTypeService
import emy.backend.barua.app.documentary.domain.model.DocumentType
import emy.backend.barua.app.documentary.domain.model.request.DocumentTypeRequest
import emy.backend.barua.app.documentary.infrastructure.ensureDocumentaryAdmin
import emy.backend.barua.route.GlobalRoute
import emy.backend.barua.app.documentary.infrastructure.route.DocumentTypeScope
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

@Tag(name = "DocumentType", description = "Types de documents")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}/")
@Profile(Mode.DEV)
class DocumentTypeController(
    private val service: DocumentTypeService,
    private val auth: Auth,
    private val sentry: SentryService,
) {
    @GetMapping(DocumentTypeScope.PUBLIC, produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll(
        request: HttpServletRequest,
        @PathVariable version: String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ResponseEntity.ok(mapOf("documentTypes" to service.findAll()))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.documenttype.getall.count",
                    distributionName = "api.documenttype.getall.latency",
                ),
            )
        }
    }
    @PostMapping(DocumentTypeScope.PRIVATE_ADMIN, consumes = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun create(
        httpRequest: HttpServletRequest,
        @Valid @RequestBody request: DocumentTypeRequest,
        @PathVariable version: String,
    ): ResponseEntity<Map<String, Any?>> {
        val startNanos = System.nanoTime()
        try {
            val userConnect = ensureDocumentaryAdmin(auth)
            val data = DocumentType(
                name = request.name,
                description = request.description,
                isActive = request.isActive,
            )
            val result = service.save(data)
            return ResponseEntity.status(201).body(
                mapOf("documentType" to result, "message" to "Enregistrement réussi")
            )
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${httpRequest.method} /${httpRequest.requestURI}",
                    countName = "api.documenttype.create.count",
                    distributionName = "api.documenttype.create.latency",
                ),
            )
        }
    }
//
//    @PutMapping("${DocumentTypeScope.PRIVATE_ADMIN}/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
//    suspend fun update(
//        httpRequest: HttpServletRequest,
//        @PathVariable version: String,
//        @PathVariable id: Long,
//        @Valid @RequestBody request: DocumentTypeRequest,
//    ): ResponseEntity<Map<String, Any?>> {
//        val startNanos = System.nanoTime()
//        try {
//            ensureDocumentaryAdmin(auth)?.let { return it }
//            if (categoryService.findById(request.categoryId) == null) {
//                return ResponseEntity.badRequest().body(mapOf("message" to "Cette catégorie est inexistante."))
//            }
//            val data = DocumentType(
//                name = request.name,
//                description = request.description,
//                isActive = request.isActive,
//            )
//            val result = service.update(id, data)
//            if (result == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to "Type de document introuvable."))
//            }
//            return ResponseEntity.ok(
//                mapOf(
//                    "documentType" to result,
//                    "message" to "Modification réussie",
//                ),
//            )
//        } finally {
//            sentry.callToMetric(
//                MetricModel(
//                    startNanos = startNanos,
//                    status = "200",
//                    route = "${httpRequest.method} /${httpRequest.requestURI}",
//                    countName = "api.documenttype.update.count",
//                    distributionName = "api.documenttype.update.latency",
//                ),
//            )
//        }
//    }
}
