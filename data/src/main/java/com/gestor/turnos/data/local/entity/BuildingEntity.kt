package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "buildings",
    foreignKeys = [
        ForeignKey(
            entity = ConsortiumEntity::class,
            parentColumns = ["id"],
            childColumns = ["consortiumId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BuildingEntity(
    @PrimaryKey
    val id: String,
    val consortiumId: String,
    val name: String,
    val address: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)