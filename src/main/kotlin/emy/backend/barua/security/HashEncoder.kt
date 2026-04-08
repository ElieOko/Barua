package emy.backend.barua.security

import emy.backend.barua.utils.*
import org.springframework.context.annotation.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.stereotype.*

@Component
@Profile(Mode.DEV)
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()
    fun encode(raw: String): String? = bcrypt.encode(raw)
    fun matches(raw: String, hashed: String): Boolean = bcrypt.matches(raw, hashed)
}