package com.gestor.turnos.domain.model

data class User(
    val id: String,
    val apartmentId: String?,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val isActive: Boolean = true
) {
    val fullName: String
        get() = "$firstName $lastName"

    fun isAdmin(): Boolean = role == UserRole.ADMIN
}