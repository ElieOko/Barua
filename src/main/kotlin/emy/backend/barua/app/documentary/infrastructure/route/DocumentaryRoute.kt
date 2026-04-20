package emy.backend.barua.app.documentary.infrastructure.route

import emy.backend.barua.route.GlobalRoute

object DocumentTypeScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${DocumentaryFeatures.DOCUMENT_TYPES_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${DocumentaryFeatures.DOCUMENT_TYPES_PATH}"
    const val PRIVATE_ADMIN = "${GlobalRoute.PRIVATE}/${DocumentaryFeatures.DOCUMENT_TYPES_PATH}"
}

object DocumentScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${DocumentaryFeatures.DOCUMENTS_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${DocumentaryFeatures.DOCUMENTS_PATH}"
    const val PRIVATE_ADMIN = "${GlobalRoute.PRIVATE}/${DocumentaryFeatures.DOCUMENTS_PATH}"
}

object ServiceDocumentaireScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${DocumentaryFeatures.SERVICE_DOCUMENTAIRES_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${DocumentaryFeatures.SERVICE_DOCUMENTAIRES_PATH}"
    const val PRIVATE_ADMIN = "${GlobalRoute.PRIVATE}/${DocumentaryFeatures.SERVICE_DOCUMENTAIRES_PATH}"
}

object DemandeProcurationScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${DocumentaryFeatures.DEMANDES_PROCURATION_PATH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${DocumentaryFeatures.DEMANDES_PROCURATION_PATH}"
    const val PRIVATE_ADMIN = "${GlobalRoute.PRIVATE}/${DocumentaryFeatures.DEMANDES_PROCURATION_PATH}"
}

object DocumentaryFeatures {
    const val DOCUMENT_TYPES_PATH = "documents/types"
    const val DOCUMENTS_PATH = "documents"
    const val SERVICE_DOCUMENTAIRES_PATH = "service/documentaires"
    const val DEMANDES_PROCURATION_PATH = "demandes/procuration"
}
