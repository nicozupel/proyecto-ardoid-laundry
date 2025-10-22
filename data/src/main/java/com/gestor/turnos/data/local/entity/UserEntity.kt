package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gestor.turnos.domain.model.UserRole

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = ApartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartmentId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class UserEntity(
    @PrimaryKey
    val id: String,
    val apartmentId: String? = null,
    val email: String,
    val passwordHash: String? = null,
    val firstName: String,
    val lastName: String,
    val role: UserRole = UserRole.USER,
    val isActive: Boolean = true,
    val fcmToken: String? = null,
    val lastLoginAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)