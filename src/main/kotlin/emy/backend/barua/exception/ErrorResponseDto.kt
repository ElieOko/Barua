package emy.backend.barua.exception

import java.time.*

data class ErrorResponseDto(
    val message : String,
    val detailMessage : String,
    val errorTime : LocalDateTime
)
