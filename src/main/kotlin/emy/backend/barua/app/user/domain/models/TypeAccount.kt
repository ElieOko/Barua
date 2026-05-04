package emy.backend.barua.app.user.domain.models

import emy.backend.barua.app.organism.domain.model.Organism
import emy.backend.barua.app.organism.domain.model.TypeOrganism
import emy.backend.barua.app.organism.infrastructure.persistance.entities.CityEntity

data class TypeAccount(
    val typeAccountId: Long? = null,
    val name: String
)

data class CompactModelKeyByAccount(val key: TypeOrganism? = null, val cityAccountOrganism : List<CompactModelCityByAccount> = emptyList())

data class CompactModelCityByAccount(
    val city : CityEntity? = null,
    val organisation : List<Organism>? = null,
    val accounts : List<Account> = emptyList(),
)