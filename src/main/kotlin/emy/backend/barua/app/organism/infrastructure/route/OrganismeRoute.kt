package emy.backend.barua.app.organism.infrastructure.route

import emy.backend.barua.route.GlobalRoute

object OrganismScope {
    const val PUBLIC = "/${GlobalRoute.PUBLIC}/${OrganismeFeatures.ORGANISM_PATH}"
    const val PRIVATE = "/${GlobalRoute.PRIVATE}/${OrganismeFeatures.ORGANISM_PATH}"
}
object OrganismTypeScope {
    const val PUBLIC = "/${GlobalRoute.PUBLIC}/${OrganismTypeFeatures.ORGANISM_TYPE_PATH}"
    const val PRIVATE = "/${GlobalRoute.PRIVATE}/${OrganismTypeFeatures.ORGANISM_TYPE_PATH}"
}

object OrganismeFeatures {
    const val ORGANISM_PATH = "organism"
}

object OrganismTypeFeatures {
    const val ORGANISM_TYPE_PATH = "organism/type"
}
