package emy.backend.barua.app.user.infrastructure.controller

import emy.backend.barua.app.user.application.services.*
import emy.backend.barua.app.user.domain.models.*
import emy.backend.barua.route.*
import emy.backend.barua.route.account.*
import emy.backend.barua.security.monitoring.*
import emy.backend.barua.utils.*
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
class TypeAccountController(
    private val service: TypeAccountService,
    private val sentry: SentryService,
) {
    @Operation(summary = "List Of TypeAccounts")
    @GetMapping(AccountTypeScope.PUBLIC,produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAllTypeAccountE(request: HttpServletRequest, @PathVariable version: String): ApiResponse<List<TypeAccount>> = coroutineScope {
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