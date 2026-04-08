package emy.backend.lawapp50.app.user.infrastructure.controller

import emy.backend.lawapp50.app.user.application.service.*
import emy.backend.lawapp50.app.user.domain.model.request.*
import emy.backend.lawapp50.security.*
import emy.backend.lawapp50.security.monitoring.*
import emy.backend.lawapp50.utils.*
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.*
import jakarta.validation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.slf4j.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@Tag(name = "Utilisateur", description = "Gestion des utilisateurs")
@RestController
@RequestMapping("api")
class UserController(
    private val userService : UserService,
    private val auth: Auth,
    private val sentry : SentryService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Liste des utilisateurs")
    @GetMapping("/{version}/protected/users")
    suspend fun getListUser(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val data = userService.findAllUser().toList()
                    ApiResponse(data)
                }
                false,null -> {
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))}
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.user.getlistuser.count",
                    distributionName = "api.user.getlistuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{version}/protected/users/{id}")
    suspend fun getUser(
        request: HttpServletRequest,
        @PathVariable("id") id : Long
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val data = userService.findIdUser(id)
                    ResponseEntity.ok().body(data)}
                false,null ->{
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))}
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.user.getuser.count",
                    distributionName = "api.user.getuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Modification utilisateur")
    @PutMapping("/{version}/protected/users/{id}")
    suspend fun updateUser(
        request: HttpServletRequest,
        @PathVariable("id") userId : Long,
        @RequestBody @Valid user : UserRequestChange
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            if (session?.first?.userId == userId || state == true ) {
                val updated = userService.updateUser(userId,user)
                ResponseEntity.ok(updated)
            }
            ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.user.updateuser.count",
                    distributionName = "api.user.updateuser.latency"
                )
            )
        }
    }

    @GetMapping("/{version}/private/users")
    suspend fun getAllUserPrivate(request: HttpServletRequest) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = userService.findAllUser().toList()
            ApiResponse(data)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.user.getAllUserPrivate.count",
                    distributionName = "api.user.getAllUserPrivate.latency"
                )
            )
        }

    }
}