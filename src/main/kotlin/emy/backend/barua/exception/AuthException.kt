package emy.backend.barua.exception

import com.fasterxml.jackson.databind.*
import jakarta.servlet.http.*
import org.springframework.security.access.*
import org.springframework.security.core.*
import org.springframework.security.web.*
import org.springframework.security.web.access.*
import org.springframework.stereotype.*
import java.io.*

@Component
class CustomAuthEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.setContentType("application/json")
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)

        val body: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        body.put("status", 401)
        body.put("error", "Unauthorized")
        body.put("message", "Accès refusé : token invalide ou manquant.")
        body.put("path", request.getServletPath())

        ObjectMapper().writeValue(response.getOutputStream(), body)
    }
}


@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class)
     override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException:  AccessDeniedException
    ) {
        response.setContentType("application/json")
        response.setStatus(HttpServletResponse.SC_FORBIDDEN)

        val body: MutableMap<String?, Any?> = HashMap<String?, Any?>()
        body.put("status", 403)
        body.put("error", "Forbidden")
        body.put("message", "Accès interdit : vous n'avez pas les autorisations nécessaires.")
        body.put("path", request.getServletPath())
        ObjectMapper().writeValue(response.getOutputStream(), body)
    }
}
