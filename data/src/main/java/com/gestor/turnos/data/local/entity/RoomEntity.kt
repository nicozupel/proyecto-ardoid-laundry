package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = BuildingEntity::class,
            parentColumns = ["id"],
            childColumns = ["buildingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RoomEntity(
    @PrimaryKey
    val id: String,
    val buildingId: String,
    val name: String,
    val floor: Int? = null,
    val openFromHour: Int = 7,
    val openToHour: Int = 23,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)