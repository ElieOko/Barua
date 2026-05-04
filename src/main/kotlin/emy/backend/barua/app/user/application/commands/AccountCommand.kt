package emy.backend.barua.app.user.application.commands

import emy.backend.barua.app.user.infrastructure.persistance.entities.*
import emy.backend.barua.app.user.infrastructure.persistance.repositories.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(4)
@Profile("dev")
class CommandLineUserComponent(
    @Value("\${spring.application.version}")  private val version: String,
    val typeAccountRepository: TypeAccountRepository,
    val accountRepository: AccountRepository,
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun run(vararg args: String) {
        log.info("commande executor **User")
        log.info(this::class.simpleName)
        log.info(version)
        try {
           /* runBlocking {
                createTypeAccountAll()
                createAccount()
            } */
        }
        catch (e : Exception){
            log.info(e.message)
        }
    }

    suspend fun createTypeAccountAll(){
        val data = typeAccountRepository.saveAll(
            listOf(
                TypeAccountEntity(name = "PrivilegedUser"),//1
                TypeAccountEntity(name = "Professional"),//2
                TypeAccountEntity(name = "Commune"),//3
                TypeAccountEntity(name = "Ministere"),//4
            )
        ).toList()
        log.info("save type account all ${data.size}")
    }

    suspend fun createAccount(){
        val store = accountRepository.saveAll<AccountEntity>(
            listOf(
                AccountEntity(name = "citoyen", typeAccountId = 2),//1
                AccountEntity(name = "admin", typeAccountId = 3),//2
                AccountEntity(name = "admin", typeAccountId = 4),//3
                AccountEntity(name = "super-admin", typeAccountId = 1),
            )
        ).toList()
        log.info("Enregistrement réussi avec succès ${store.size}")
    }
}