package emy.backend.barua.app.documentary.infrastructure

import emy.backend.barua.security.Auth
import kotlinx.coroutines.coroutineScope
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException

/**
 * Même règle que les routes utilisateurs « admin » : premier compte lié avec [AccountService.isAllow].
 */
suspend fun ensureDocumentaryAdmin(auth: Auth) = coroutineScope {
    val session = auth.user() ?: throw ResponseStatusException(HttpStatusCode.valueOf(403), "Authentification requise.")
    if (session.second.find { it } != true) throw ResponseStatusException(HttpStatusCode.valueOf(403),"Accès réservé aux administrateurs.")
    session
}
