package com.gestor.turnos.domain.model

data class Machine(
    val id: String,
    val roomId: String,
    val name: String,
    val type: MachineType,
    val status: MachineState,
    val esp32Id: String? = null,
    val isActive: Boolean = true
)