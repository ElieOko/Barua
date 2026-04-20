package emy.backend.barua.app.user.infrastructure.persistance.entities

import com.fasterxml.jackson.annotation.*
import org.springframework.data.annotation.*
import org.springframework.data.relational.core.mapping.*
import java.time.*

@Table(name = "users")
class UserEntity(
    @Id
    @Column("id")
    val userId: Long? = null,
    @JsonIgnore
    @Column("password")
    var password: String? = "",
    @Column("email")
    var email: String? = null,
    @Column("username")
    var username: String? = null,
    @Column("first_name")
    var firstName: String,
    @Column("last_name")
    var lastName: String,
    @Column("full_name")
    var fullName: String,
    @Column("from_service")
    var fromService : String? = null,
    @Column("is_premium")
    var isPremium: Boolean = false,
    @Column("is_certified")
    var isCertified: Boolean = false,
    @Column("is_lock")
    var isLock: Boolean = false,
    @Column("is_valid")
    var isValid: Boolean = false,
    @Column("phone")
    var phone: String?=null,
    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
