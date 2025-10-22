package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "apartments",
    foreignKeys = [
        ForeignKey(
            entity = BuildingEntity::class,
            parentColumns = ["id"],
            childColumns = ["buildingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ApartmentEntity(
    @PrimaryKey
    val id: String,
    val buildingId: String,
    val floor: Int,
    val number: String,
    val fullNumber: String, // e.g., "4A", "12B"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)