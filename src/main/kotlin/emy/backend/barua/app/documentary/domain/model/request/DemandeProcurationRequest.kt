package emy.backend.barua.app.documentary.domain.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

class DemandeProcurationRequest {
    @field:NotNull(message = "Le service documentaire est obligatoire")
    var serviceDocumentaireId: Long? = null

    var commentaire: String? = null
    var fullName: String? = null
    var numberIdentity: String? = null
    var phone: String? = null

    @field:Schema(
        description = "Pièces jointes (images, PDF, documents Word)",
        type = "array",
        format = "binary",
    )
    var piece: List<MultipartFile>? = null
}
