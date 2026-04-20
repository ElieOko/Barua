package emy.backend.barua.app.documentary.infrastructure

import emy.backend.barua.security.Auth
import org.springframework.http.ResponseEntity

/**
 * Même règle que les routes utilisateurs « admin » : premier compte lié avec [AccountService.isAllow].
 */
suspend fun ensureDocumentaryAdmin(auth: Auth): ResponseEntity<Map<String, Any?>>? {
    val session = auth.user()
        ?: return ResponseEntity.status(401).body(mapOf("message" to "Authentification requise."))
    if (session.second.find { true } != true) {
        return ResponseEntity.status(403).body(mapOf("message" to "Accès réservé aux administrateurs."))
    }
    return null
}
