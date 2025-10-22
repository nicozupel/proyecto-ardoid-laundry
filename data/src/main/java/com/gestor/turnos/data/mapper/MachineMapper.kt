package com.gestor.turnos.data.mapper

import com.gestor.turnos.data.local.entity.MachineEntity
import com.gestor.turnos.domain.model.Machine
import javax.inject.Inject

class MachineMapper @Inject constructor() {

    fun toDomain(entity: MachineEntity): Machine {
        return Machine(
            id = entity.id,
            roomId = entity.roomId,
            name = entity.name,
            type = entity.type,
            status = entity.status,
            esp32Id = entity.esp32Id,
            isActive = entity.isActive
        )
    }

    fun toEntity(domain: Machine): MachineEntity {
        return MachineEntity(
            id = domain.id,
            roomId = domain.roomId,
            name = domain.name,
            type = domain.type,
            status = domain.status,
            esp32Id = domain.esp32Id,
            isActive = domain.isActive
        )
    }
}