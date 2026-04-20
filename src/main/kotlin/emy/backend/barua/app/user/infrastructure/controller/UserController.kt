package emy.backend.barua.app.user.infrastructure.controller

import emy.backend.barua.app.user.application.services.*
import emy.backend.barua.app.user.domain.models.request.*
import emy.backend.barua.route.*
import emy.backend.barua.security.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.*
import jakarta.validation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "Managements Users")
@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
class UserController(
    private val userService : UserService,
    private val auth: Auth,
    private val sentry : SentryService
) {
    @Operation(summary = "List of users")
    @GetMapping("/protected/users")
    suspend fun getListUser(request: HttpServletRequest, @PathVariable version: String) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> ApiResponse(userService.findAllUser().toList())
                else -> ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
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

    @Operation(summary = "Detail user")
    @GetMapping("/protected/users/{id}")
    suspend fun getUser(
        request: HttpServletRequest,
        @PathVariable id:Long,
        @PathVariable version:String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> ResponseEntity.ok().body(userService.findIdUser(id))
                else -> ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
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
    @PutMapping("/protected/users/{id}")
    suspend fun updateUser(
        request: HttpServletRequest,
        @PathVariable("id") userId : Long,
        @RequestBody @Valid user : UserRequestChange,
        @PathVariable version: String
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            if (session?.first?.userId == userId || state == true) {
                val updated = userService.updateUser(userId, user)
                ResponseEntity.ok(updated)
            } else {
                ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
            }
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

    @GetMapping("/private/users")
    suspend fun getAllUserPrivate(request: HttpServletRequest, @PathVariable version: String) = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ApiResponse(userService.findAllUser().toList())
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