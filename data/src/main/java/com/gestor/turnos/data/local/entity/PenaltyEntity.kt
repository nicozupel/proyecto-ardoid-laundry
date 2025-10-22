package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "penalties",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BookingEntity::class,
            parentColumns = ["id"],
            childColumns = ["bookingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PenaltyEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val bookingId: String,
    val points: Int,
    val reason: String,
    val expiresAt: Long,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)