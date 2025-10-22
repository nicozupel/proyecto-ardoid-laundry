package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType

@Entity(
    tableName = "machines",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MachineEntity(
    @PrimaryKey
    val id: String,
    val roomId: String,
    val name: String,
    val type: MachineType,
    val status: MachineState = MachineState.LIBRE,
    val esp32Id: String? = null,
    val wattsOnThreshold: Double = 50.0,
    val wattsOffThreshold: Double = 10.0,
    val vibrationOnThreshold: Double = 5.0,
    val vibrationOffThreshold: Double = 1.0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)