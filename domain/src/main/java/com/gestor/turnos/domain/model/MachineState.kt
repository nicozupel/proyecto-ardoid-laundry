package com.gestor.turnos.domain.model

enum class MachineState {
    LIBRE,
    RESERVADO,
    EN_USO,
    GRACIA,
    MANTENIMIENTO,
    FUERA_DE_SERVICIO,
    BLOQUEADA
}