package emy.backend.barua.app.user.infrastructure.persistance.mapper

import emy.backend.barua.app.user.domain.models.User
import emy.backend.barua.app.user.domain.models.UserDto
import emy.backend.barua.app.user.infrastructure.persistance.entities.UserEntity

private fun buildFullName(firstName: String, lastName: String, fallback: String?): String {
    val full = "${firstName.trim()} ${lastName.trim()}".trim()
    return full.ifBlank { fallback?.trim().orEmpty().ifBlank { "Utilisateur" } }
}

fun UserEntity.toDomain(): UserDto {
    val entity = this
    return UserDto(
        userId = entity.userId,
        email = entity.email,
        phone = entity.phone,
        username = entity.username.orEmpty(),
        city = "",
        firstName = entity.firstName,
        lastName = entity.lastName,
        isPremium = entity.isPremium,
        isCertified = entity.isCertified,
    )
}

fun UserDto.toEntityToDto(password: String): UserEntity {
    val user = this
    return UserEntity(
        userId = user.userId,
        username = user.username,
        email = user.email,
        phone = user.phone,
        firstName = user.firstName,
        lastName = user.lastName,
        fullName = buildFullName(user.firstName, user.lastName, user.username),
        password = password,
        isPremium = user.isPremium,
        isCertified = user.isCertified,
    )
}

fun User.toEntity(): UserEntity {
    val user = this
    return UserEntity(
        userId = user.userId.takeIf { it > 0 },
        username = user.username,
        email = user.email,
        phone = user.phone,
        firstName = user.firstName,
        lastName = user.lastName,
        fullName = buildFullName(user.firstName, user.lastName, user.username ?: user.email),
        password = user.password,
    )
}
