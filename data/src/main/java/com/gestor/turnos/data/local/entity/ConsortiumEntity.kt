package com.gestor.turnos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consortiums")
data class ConsortiumEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)