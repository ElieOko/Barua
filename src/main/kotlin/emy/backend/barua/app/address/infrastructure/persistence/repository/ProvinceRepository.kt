package emy.backend.barua.app.address.infrastructure.persistence.repository

import emy.backend.barua.app.address.infrastructure.persistence.entity.ProvinceEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProvinceRepository : CoroutineCrudRepository<ProvinceEntity,Long>