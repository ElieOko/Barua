package emy.backend.lawapp50.app.user.infrastructure.controller

import emy.backend.lawapp50.app.user.application.service.TypeAccountService
import emy.backend.lawapp50.app.user.domain.model.TypeAccount
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import emy.backend.lawapp50.route.account.AccountTypeScope
import emy.backend.lawapp50.utils.ApiResponse
import emy.backend.lawapp50.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import emy.backend.lawapp50.security.monitoring.MetricModel


@RestController
@RequestMapping("api")
@Profile("dev")
class TypeAccountController(
    private val service: TypeAccountService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste de Type Accounts")
    @GetMapping("/{version}/${AccountTypeScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeAccountE(request: HttpServletRequest): ApiResponse<List<TypeAccount>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            ApiResponse(service.getAll().toList())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.typeaccount.getalltypeaccounte.count",
                    distributionName = "api.typeaccount.getalltypeaccounte.latency"
                )
            )
        }
    }
}