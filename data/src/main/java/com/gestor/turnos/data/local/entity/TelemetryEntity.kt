package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "telemetry",
    foreignKeys = [
        ForeignKey(
            entity = MachineEntity::class,
            parentColumns = ["esp32Id"],
            childColumns = ["esp32Id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TelemetryEntity(
    @PrimaryKey
    val id: String,
    val esp32Id: String,
    val timestamp: Long,
    val watts: Double,
    val vibration: Double,
    val state: String, // RUNNING, IDLE, OFF
    val temperature: Double? = null,
    val humidity: Double? = null,
    val createdAt: Long = System.currentTimeMillis()
)