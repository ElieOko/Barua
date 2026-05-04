package emy.backend.barua.app.user.application.services

import emy.backend.barua.app.organism.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.organism.infrastructure.persistance.repository.CityRepository
import emy.backend.barua.app.organism.infrastructure.persistance.repository.OrganismRepository
import emy.backend.barua.app.organism.infrastructure.persistance.repository.TypeOrganismRepository
import emy.backend.barua.app.user.domain.models.*
import kotlinx.coroutines.flow.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.stereotype.*
import org.springframework.web.server.*
import emy.backend.barua.utils.*
import emy.backend.barua.app.user.infrastructure.persistance.entities.TypeAccountEntity
import emy.backend.barua.app.user.infrastructure.persistance.entities.toDomain
import emy.backend.barua.app.user.infrastructure.persistance.mapper.toDomain
import emy.backend.barua.app.user.infrastructure.persistance.mapper.toEntity
import emy.backend.barua.app.user.infrastructure.persistance.repositories.AccountRepository
import emy.backend.barua.app.user.infrastructure.persistance.repositories.TypeAccountRepository
import kotlinx.coroutines.coroutineScope
import java.util.Locale.getDefault

@Service
@Profile(Mode.DEV)
class TypeAccountService(
    private val repository: TypeAccountRepository,
    private val account: AccountRepository,
    private val organism: OrganismRepository,
    private val typeOrganism: TypeOrganismRepository,
    private val city : CityRepository
) {
    suspend fun saveAccount(data: TypeAccount): TypeAccount {
        val data = data.toEntity()
        val result = repository.save(data)
        return result.toDomain()
    }
    suspend fun groupByKey() = coroutineScope {
        val data = mutableListOf<CompactModelKeyByAccount>()
        val dataCityAccount = mutableListOf<CompactModelCityByAccount>()
        typeOrganism.findAll().toList().forEach {
            when(it.name.lowercase(getDefault())){
                TypeOrganismInstance.COMMUNE.name.lowercase(getDefault()) -> {
                    //organism
                    city.findAll().toList().forEach {c->
                        val org = organism.findByCityIdAndTypeId(c.id!!,it.id!!)
                        val acc = repository.findById(3)
                        if(acc != null && org != null){
                           val items = account.findAll().toList().filter { r-> r.typeAccountId == acc.id}.map { r -> r.toDomain() }
                           val state = org.map{orga->orga.toDomain()}.toList()
                            if (state.isNotEmpty()) dataCityAccount.add(CompactModelCityByAccount(city = c, accounts = items, organisation = state))
                        }
                    }
                    data.add(CompactModelKeyByAccount(key = it.toDomain(),dataCityAccount))
                }
                TypeOrganismInstance.MINISTERE.name.lowercase(getDefault()), "Ministère".lowercase(getDefault()) ->{
//                    data.add(CompactModelKeyByAccount())
                }
                TypeOrganismInstance.SERVICE_PUBLIC.name.lowercase(getDefault()), "Service Publique".lowercase(getDefault()) ->{
//                    data.add(CompactModelKeyByAccount())
                }
                TypeOrganismInstance.MAIRIE.name.lowercase(getDefault()) ->{
//                    data.add(CompactModelKeyByAccount())
                }

            }
        }
        data
    }
    suspend fun getAll(): Flow<TypeAccount> {
        val data= repository.findAll()
        return data.map {
            TypeAccountEntity(it.id, it.name).toDomain()
        }
    }
    suspend fun findByIdTypeAccount(id : Long) : TypeAccount {
      val data = repository.findById(id) ?: throw ResponseStatusException(
          HttpStatusCode.valueOf(404),
          "ID Is Not Found."
      )
        return data.toDomain()
    }
}