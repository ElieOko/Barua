package emy.backend.lawapp50.app.user.infrastructure.controller

import emy.backend.lawapp50.app.user.application.service.*
import emy.backend.lawapp50.app.user.domain.model.*
import emy.backend.lawapp50.app.user.domain.model.request.*
import emy.backend.lawapp50.app.user.domain.model.toDomain
import emy.backend.lawapp50.app.user.infrastructure.persistance.repository.*
import emy.backend.lawapp50.route.auth.*
import emy.backend.lawapp50.route.auth.AuthRoute.LOGIN_AUTH_GOOGLE
import emy.backend.lawapp50.security.*
import emy.backend.lawapp50.security.monitoring.*
import emy.backend.lawapp50.utils.*
import emy.backend.lawapp50.utils.mail.*
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.tags.*
import jakarta.servlet.http.*
import jakarta.validation.*
import kotlinx.coroutines.*
import org.slf4j.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.security.core.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import server.web.casa.adaptater.provide.redis.*

const val ROUTE_REGISTER = AuthRoute.REGISTER
const val ROUTE_LOGIN = AuthRoute.LOGIN
@Tag(name = "Authentification", description = "Gestion des accès")
@RestController
@RequestMapping
@Profile("dev")
class AuthController(
    private val authService: AuthService,
//    private val accountService: TypeAccountService,
    private val auth: Auth,
    private val sentry : SentryService,
    private val senderMailAuth : SenderMailAuth,
    private val userRepository: UserRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)
    @Operation(summary = "Création utilisateur")
    @PostMapping(ROUTE_REGISTER)
    suspend fun register(request: HttpServletRequest,
        @Valid @RequestBody req : UserRequest
    ): ResponseEntity<Map<String, Any?>> = coroutineScope {
        val startNanos = System.nanoTime()
        val redis = RedisStorage()
        try {
            if (!isEmailValid(req.email)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail invalid!.")
            val userSystem = req.toDomain()
            val state = req.confirmPassword == req.password
            if (!state) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Mot de passe invalide.")
            val data = authService.register(userSystem)
            val generator = 6.generateOtp()
            redis.storeRedisData(data.email!!,generator,1140)
            val sendState = senderMailAuth.sendMail(to = data.email!!,otp = generator, time =  "4")
            log.info("$sendState************")
            val response = mapOf(
                "user" to data,
                "message" to "Votre compte utilisateur principal a été créé avec succès. Par ailleurs, nous avons envoyé un code de vérification à votre adresse e-mail."
            )
            ResponseEntity.status(201).body(response)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.register.count",
                    distributionName = "api.auth.register.latency"
                )
            )
        }
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping(ROUTE_LOGIN)
    suspend fun login(
        request: HttpServletRequest,
      @Valid @RequestBody body: UserAuth
    ): ResponseEntity<Map<String, Any?>>  = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val data = authService.login(body.identifiant, body.password)
              try {
                  val response = mapOf("member" to data.second, "token" to data.first.accessToken, "refresh_token" to data.first.refreshToken, "message" to "Connexion réussie avec succès")
                  ResponseEntity.ok().body(response)
              }
              catch (e: AuthenticationException){
                  log.info(e.message)
                  val response = mapOf("message" to e.message)
                  ResponseEntity.status(401).body(response)
              }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.login.count",
                    distributionName = "api.auth.login.latency"
                )
            )
        }
    }

    @PostMapping("/api/{version}/protected/token/refresh")
    suspend fun refresh(request: HttpServletRequest, @RequestBody body: RefreshRequest): AuthService.TokenPair = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            authService.refresh(body.refreshToken)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.refresh.count",
                    distributionName = "api.auth.refresh.latency"
                )
            )
        }
    }

//    @PostMapping("/api/{version}/public/callback/google")
//    suspend fun callBackGoogle(request: HttpServletRequest,@RequestBody body: Any?){
//        val startNanos = System.nanoTime()
//        try {
//
//        } finally {
//            sentry.callToMetric(
//                MetricModel(
//                    startNanos = startNanos,
//                    status = "200",
//                    route = "${request.method} /${request.requestURI}",
//                    countName = "api.auth.callBackGoogle.count",
//                    distributionName = "api.auth.callBackGoogle.latency"
//                )
//            )
//        }
//    }
//
//    @PostMapping("/api/{version}/public/callback/apple")
//    suspend fun callBackApple(request: HttpServletRequest,@RequestBody body: Any?){
//        val startNanos = System.nanoTime()
//        try {
//
//        } finally {
//            sentry.callToMetric(
//                MetricModel(
//                    startNanos = startNanos,
//                    status = "200",
//                    route = "${request.method} /${request.requestURI}",
//                    countName = "api.auth.callBackApple.count",
//                    distributionName = "api.auth.callBackApple.latency"
//                )
//            )
//        }
//    }

    @GetMapping("/api/auth/google")
    fun redirectToGoogle(response: HttpServletResponse) {
        response.sendRedirect("/oauth2/authorization/google")
    }
//    @GetMapping(LOGIN_AUTH_GOOGLE)
//    fun codeGoogle(response: HttpServletResponse) {
//
//    }
    @Operation(summary = "OTP validate account")
    @PostMapping("/api/{version}/public/otp/validate")
    suspend fun validationAccountOTP(
        request: HttpServletRequest,
        @RequestBody @Valid identifier : VerifyRequest
    ) = coroutineScope {
        val startNanos = System.nanoTime()
        val redis = RedisStorage()
        try {
            val result = redis.getRedisData(identifier.identifier) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Indentifiant invalide !"
            )
            if (result != identifier.code) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Code invalide !")
            val user =userRepository.findByPhoneOrEmail(identifier.identifier)
            user?.isValid = true
            userRepository.save(user!!)
            val message = mapOf("message" to "votre compte a été valider avec succès")
            redis.delete(identifier.identifier)
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.generatekeyotp.count",
                    distributionName = "api.auth.generatekeyotp.latency"
                )
            )
        }
    }
    @Operation(summary = "OTP activation send code")
    @PostMapping("/api/{version}/public/otp/generate")
    suspend fun generateKeyOTP(request: HttpServletRequest,
        @RequestBody @Valid user : IdentifiantRequest
    ): ResponseEntity<Map<String, String?>> = coroutineScope {
        val startNanos = System.nanoTime()
        val redis = RedisStorage()
        try {
            if (!isEmailValid(user.identifier)) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail invalid!.")
            val result = userRepository.findByPhoneOrEmail(user.identifier)?:throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifiant invalide !.")
            val generator = 6.generateOtp()
            redis.storeRedisData(user.identifier,generator,1140)
            val sendState = senderMailAuth.sendMail(to = result.email!!,otp = generator, time =  "4")
            val message = mapOf("message" to "Un code de validation a été envoyé cet adresse")
             ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.generatekeyotp.count",
                    distributionName = "api.auth.generatekeyotp.latency"
                )
            )
        }
    }
//
//    @Operation(summary = "OTP activation send code")
//    @PostMapping("/api/{version}/public/otp/verify")
//    suspend fun verifyOTP(request: HttpServletRequest,
//        @RequestBody @Valid user : VerifyRequest
//    ): ResponseEntity<out Map<String, Any?>> = coroutineScope {
//        val startNanos = System.nanoTime()
//        try {
//            val result = authService.verifyOTP(user)
//            val message = mapOf("status" to result.second, "user" to result.first)
//            ResponseEntity.ok(message)
//        } finally {
//            sentry.callToMetric(
//                MetricModel(
//                    startNanos = startNanos,
//                    status = "200",
//                    route = "${request.method} /${request.requestURI}",
//                    countName = "api.auth.verifyotp.count",
//                    distributionName = "api.auth.verifyotp.latency"
//                )
//            )
//        }
//    }
    @Operation(summary = "certification user")
    @PutMapping("/api/{version}/protected/certification/{userId}")
    suspend fun goCertification(
        request: HttpServletRequest,
        @PathVariable("userId") userId : Long,
        @RequestBody @Valid certification : CertificationState
    )  = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val data = authService.goCertification(userId,certification.state)
                    ResponseEntity.ok(
                        mapOf(
                            "message" to if (certification.state) "Certification successful" else "Certification failed",
                            "user" to data
                        )
                    )
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
                    countName = "api.certifcation.user.count",
                    distributionName = "api.certifcation.user.latency"
                )
            )
        }

    }

    @Operation(summary = "Reset password ")
    @PutMapping("/api/{version}/protected/reset/password")
    suspend fun resetPassword(request: HttpServletRequest,
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val new = user.newPassword
            authService.changePassword(session?.first?.userId?:0,new)
            val message = mapOf("message" to "Mot de passe changé avec succès")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.resetpassword.count",
                    distributionName = "api.auth.resetpassword.latency"
                )
            )
        }
    }

    @Operation(summary = "Change password utilisateur")
    @PutMapping("/api/{version}/protected/change/password")
    suspend fun updateUser(request: HttpServletRequest,
        @RequestBody @Valid user : UserPassword
    ) : ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userConnect = auth.user()
            val new = user.newPassword
            authService.changePassword(userConnect?.first?.userId!!,new)
            val message = mapOf("message" to "Mot de passe changé avec succès")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.updateuser.count",
                    distributionName = "api.auth.updateuser.latency"
                )
            )
        }
    }

    @Operation(summary = "Delete Account User")
    @DeleteMapping("/api/{version}/protected/users/delete/user")
    suspend fun lockAccount(request: HttpServletRequest): ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val userId = auth.user()?.first?.userId
            val state = authService.lockedOrUnlocked(userId as Long)
            val message = mapOf("message" to if (state) "Votre compte a été supprimé avec succès" else "Cet utilisateur n'existe pas")
            ResponseEntity.ok(message)
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.lockaccount.count",
                    distributionName = "api.auth.lockaccount.latency"
                )
            )
        }
    }

    @Operation(summary = "Recovery Account User")
    @PutMapping("/api/{version}/protected/recovery/user/{id}")
    suspend fun unlockAccount(request: HttpServletRequest,@PathVariable("id") id : Long): ResponseEntity<Map<String, String>> = coroutineScope {
        val startNanos = System.nanoTime()
        try {
            val session = auth.user()
            val state: Boolean? = session?.second?.find{ true }
            when (state) {
                true -> {
                    val state = authService.lockedOrUnlocked(id,false)
                    val message = mapOf("message" to if (state) "Votre compte a été restauré avec succès" else "Cet utilisateur n'existe pas")
                    ResponseEntity.ok(message)
                }
                false,null -> {
                    ResponseEntity.status(403).body(mapOf("message" to "Accès non autorisé"))
                }
            }
        } finally {
            sentry.callToMetric(
                MetricModel(
                    startNanos = startNanos,
                    status = "200",
                    route = "${request.method} /${request.requestURI}",
                    countName = "api.auth.unlockaccount.count",
                    distributionName = "api.auth.unlockaccount.latency"
                )
            )
        }
    }
}