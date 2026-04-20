package emy.backend.barua.app.organism.application.commandes

import emy.backend.barua.app.organism.infrastructure.persistance.entities.*
import emy.backend.barua.app.organism.infrastructure.persistance.repository.*
import emy.backend.barua.utils.Mode
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(4)
@Profile(Mode.DEV)
class OrganismCmd(
    private val organism : OrganismRepository,
    private val typeOrganism: TypeOrganismRepository,
    private val city: CityRepository,
) : CommandLineRunner {
    override fun run(vararg args: String) {
        runBlocking {
//            createTypeOrganism()
            //createCity()
            //createOrganism()
        }
    }
    private suspend fun createOrganism(){
        organism.saveAll(listOf(
            OrganismEntity(id = null, typeId = 2, name = "Bandalungwa", cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Barumbu",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Bumbu",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Gombe",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kalamu",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kasa-Vubu",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kimbanseke",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kinshasa",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kintambo",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Kisenso",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Lemba",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Limete",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Lingwala",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Makala",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Maluku",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Masina",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Matete",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Mont Ngafula",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Ndjili",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Ngaba",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Ngaliema",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Ngiri-Ngiri",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Nsele",cityId = 1),
            OrganismEntity(id = null, typeId = 2, name = "Selembao",cityId = 1)
        )).toList()
    }

    private suspend fun createTypeOrganism(){
        typeOrganism.saveAll(listOf(
                TypeOrganismEntity(
                    id = null,
                    name = "Commune",
                    description = "",
                ),
                TypeOrganismEntity(
                    id = null,
                    name = "Mairie",
                    description = "",
                ),
                TypeOrganismEntity(
                    id = null,
                    name = "Ministère",
                    description = ""
                ),
                TypeOrganismEntity(
                    id = null,
                    name = "Service Publique",
                    description = "",
                ),
            )).toList()
    }

    private suspend fun createCity(){
        city.saveAll(listOf(
            CityEntity(name = "Kinshasa"),
            CityEntity(name = "Lubumbashi"),
            CityEntity(name = "Matadi"),
            CityEntity(name = "Kolwezi")
        )).toList()
    }

}