package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gestor.turnos.domain.model.BookingStatus

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = MachineEntity::class,
            parentColumns = ["id"],
            childColumns = ["machineId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BookingEntity(
    @PrimaryKey
    val id: String,
    val machineId: String,
    val userId: String,
    val startDateTime: Long, // Timestamp in milliseconds
    val endDateTime: Long,
    val status: BookingStatus = BookingStatus.RESERVADO,
    val version: Int = 1, // For optimistic locking
    val actualStartTime: Long? = null,
    val actualEndTime: Long? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)