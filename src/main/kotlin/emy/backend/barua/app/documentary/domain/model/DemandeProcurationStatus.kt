package emy.backend.barua.app.documentary.domain.model

data class DemandeProcurationStatus(
    val statusId: Long,
    val code: String,
    val label: String,
    val isActive: Boolean = true,
)

data class DemandeProcurationStatusDto(
    val statusId: Long,
    val code: String,
    val label: String,
)

fun DemandeProcurationStatus.toDto() = DemandeProcurationStatusDto(
    statusId = statusId,
    code = code,
    label = label,
)
