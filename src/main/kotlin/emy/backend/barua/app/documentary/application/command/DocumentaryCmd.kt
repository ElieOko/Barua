package emy.backend.barua.app.documentary.application.command

import emy.backend.barua.app.documentary.infrastructure.persistence.entity.*
import emy.backend.barua.app.documentary.infrastructure.persistence.repository.*
import emy.backend.barua.utils.Mode
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(5)
@Profile(Mode.DEV)
class DocumentaryCmd(
    private val document: DocumentRepository,
    private val typeDocument: DocumentTypeRepository
) : CommandLineRunner {
    override fun run(vararg args: String) {
        runBlocking {
            //createDocumentaryType()
            //createDocumentary()
        }
    }
    private suspend fun createDocumentary() {
        document.saveAll(listOf(
            DocumentEntity(documentTypeId = 1, title = "Acte de décès", description = "Document officiel attestant le décès d’une personne."),
            DocumentEntity(documentTypeId = 1, title = "Acte de mariage", description = "Document officiel prouvant l’union légale entre deux personnes."),
            DocumentEntity(documentTypeId = 1, title = "Acte de naissance", description = "Document officiel attestant la naissance d’une personne."),
            DocumentEntity(documentTypeId = 1, title = "Acte de notoriété supplétif à l’acte de décès", description = "Document établi en absence d’acte de décès pour attester un décès."),
            DocumentEntity(documentTypeId = 1, title = "Acte de notoriété supplétif à l’acte de mariage", description = "Document établi en absence d’acte de mariage pour attester une union."),
            DocumentEntity(documentTypeId = 1, title = "Acte de notoriété supplétif à l’acte de naissance", description = "Document établi en absence d’acte de naissance pour attester une naissance."),
            DocumentEntity(documentTypeId = 1, title = "Acte de reconnaissance d’un enfant", description = "Document par lequel un parent reconnaît officiellement un enfant."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de célibataire", description = "Document certifiant qu’une personne n’est pas mariée."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de changement de nom", description = "Document attestant le changement officiel de nom d’une personne."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de composition familiale", description = "Document indiquant les membres d’un ménage ou d’une famille."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de coutume", description = "Document certifiant une situation conforme aux coutumes locales."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de divorce", description = "Document attestant la dissolution d’un mariage."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de logement", description = "Document prouvant qu’une personne réside à une adresse donnée."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de naissance", description = "Document simplifié attestant la naissance d’une personne."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de sous-tutelle", description = "Document indiquant qu’une personne est placée sous tutelle."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de veuvage", description = "Document certifiant qu’une personne est veuve."),
            DocumentEntity(documentTypeId = 1, title = "Attestation de vie", description = "Document prouvant qu’une personne est en vie."),
            DocumentEntity(documentTypeId = 1, title = "Copie d’acte de décès", description = "Reproduction officielle de l’acte de décès."),
            DocumentEntity(documentTypeId = 1, title = "Copie d’acte de mariage", description = "Reproduction officielle de l’acte de mariage."),
            DocumentEntity(documentTypeId = 1, title = "Copie d’acte de naissance", description = "Reproduction officielle de l’acte de naissance."),
            DocumentEntity(documentTypeId = 1, title = "Extrait d’acte de décès", description = "Résumé officiel des informations principales d’un acte de décès."),
            DocumentEntity(documentTypeId = 1, title = "Extrait d’acte de mariage", description = "Résumé officiel des informations principales d’un acte de mariage."),
            DocumentEntity(documentTypeId = 1, title = "Extrait d’acte de naissance", description = "Résumé officiel des informations principales d’un acte de naissance.")
        )).toList()
    }

    private suspend fun createDocumentaryType() {
        typeDocument.saveAll(listOf(
            DocumentTypeEntity(
                name = "État Civil & Papiers",
                description = "Actes de naissance, mariage, nationalité, passeport, carte d'identité..."
            ),
            DocumentTypeEntity(
                name = "Emploi & Sécurité Sociale",
                description = "Volontariat, carte de demandeur d'emploi, cotisation sociale..."
            ),
            DocumentTypeEntity(
                name = "Éducation & Formation",
                description = "Examens, concours, bourses d'études, équivalences de diplômes..."
            ),
            DocumentTypeEntity(
                name = "Fiscalité, Foncier & Douanes",
                description = "Titre foncier, justificatif fiscal, taxes, redevances..."
            ),
            DocumentTypeEntity(
                name = "Agriculture, Élevage & Industrie",
                description = "Enrôlement des petits agriculteurs, subventions, licences..."
            ),
            DocumentTypeEntity(name = "Justice & Droit"),
            DocumentTypeEntity(name = "Habitat & Transport"),
            DocumentTypeEntity(name = "Télécommunication & Culture"),
            DocumentTypeEntity(name = "Sécurité & Ordre Public"),
            DocumentTypeEntity(name = "Santé & Protection Sociale"),
        )).toList()
    }
}