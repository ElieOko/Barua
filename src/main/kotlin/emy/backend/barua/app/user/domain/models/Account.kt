package emy.backend.barua.app.user.domain.models

import emy.backend.barua.app.organism.domain.model.Organism
import emy.backend.barua.app.organism.domain.model.OrganismDAO
import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountDTO
import emy.backend.barua.app.user.infrastructure.persistance.entities.AccountEntity

data class Account(
    val id : Long? = null,
    val name: String,
    val typeAccountId : Long
)

data class AccountDAO(
    val id : Long? = null,
    val name: String,
    val type: String,
)

fun Account.toEntity() = AccountEntity(
    id = this.id,
    name = this.name,
    typeAccountId = this.typeAccountId,
)

data class AccountUserDTO(
    val account: AccountDTO,
    val organism: OrganismDAO?,
)