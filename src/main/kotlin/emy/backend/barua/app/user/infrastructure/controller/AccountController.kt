package emy.backend.barua.app.user.infrastructure.controller

import emy.backend.barua.app.user.application.services.*
import emy.backend.barua.app.user.infrastructure.persistance.entities.*
import emy.backend.barua.route.*
import emy.backend.barua.route.account.*
import emy.backend.barua.security.monitoring.*
import io.swagger.v3.oas.annotations.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.*

@RestController
@RequestMapping("${GlobalRoute.ROOT}/{version}")
@Profile("dev")
class AccountController(
    private val service: AccountService,
    private val sentry: SentryService,
) {
    @Operation(summary = "List of accounts")
    @GetMapping(AccountScope.PUBLIC,produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllAccountE(request: HttpServletRequest, @PathVariable version: String): Map<String, List<AccountDTO>> = coroutineScope {
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