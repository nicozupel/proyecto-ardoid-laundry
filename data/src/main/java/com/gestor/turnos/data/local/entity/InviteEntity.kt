package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "invites",
    foreignKeys = [
        ForeignKey(
            entity = ApartmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartmentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["usedByUserId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class InviteEntity(
    @PrimaryKey
    val code: String,
    val apartmentId: String,
    val expiresAt: Long,
    val usedByUserId: String? = null,
    val usedAt: Long? = null,
    val createdBy: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)