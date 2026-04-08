package emy.backend.barua.adaptater.provider.twilio

import com.twilio.*
import jakarta.annotation.*
import org.springframework.beans.factory.annotation.*
import org.springframework.context.annotation.*

@Configuration
class TwilioConfig(
    // reading the Twilio ACCOUNT SID from application.properties
    @Value("\${twilio.account-sid}") val accountSid: String,
    // reading the Twilio AUTH TOKEN from application.properties
    @Value("\${twilio.auth-token}") val authToken: String,
) {
    @PostConstruct
    fun twilioInit() {
        // initializing Twilio
        Twilio.init(
            accountSid,
            authToken
        )
    }
}