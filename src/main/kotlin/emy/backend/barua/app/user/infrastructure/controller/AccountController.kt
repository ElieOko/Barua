package server.web.casa.app.user.infrastructure.controller

import emy.backend.lawapp50.app.user.application.service.AccountService
import emy.backend.lawapp50.app.user.infrastructure.persistance.entity.AccountDTO
import emy.backend.lawapp50.route.account.AccountScope
import io.swagger.v3.oas.annotations.Operation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import emy.backend.lawapp50.security.monitoring.SentryService
import jakarta.servlet.http.HttpServletRequest
import emy.backend.lawapp50.security.monitoring.MetricModel

@RestController
@RequestMapping("api")
@Profile("dev")
class AccountController(
    private val service: AccountService,
    private val sentry: SentryService,
) {
    @Operation(summary = "Liste de Accounts")
    @GetMapping("/{version}/${AccountScope.PUBLIC}",produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(request: HttpServletRequest): Map<String, List<AccountDTO>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            mapOf("accounts" to service.getAll().toList())
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.account.getallaccounte.count",
                    distributionName = "api.account.getallaccounte.latency"
                )
            )
        }
    }
}