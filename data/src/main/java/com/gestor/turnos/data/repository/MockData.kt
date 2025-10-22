package com.gestor.turnos.data.repository

import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import java.time.LocalDateTime

object MockData {

    fun getMockMachines(): List<Machine> = listOf(
        Machine(
            id = "machine1",
            roomId = "room1",
            name = "Lavarropas 1",
            type = MachineType.LAVARROPAS,
            status = MachineState.LIBRE,
            esp32Id = "esp32_1"
        ),
        Machine(
            id = "machine2",
            roomId = "room1",
            name = "Lavarropas 2",
            type = MachineType.LAVARROPAS,
            status = MachineState.EN_USO,
            esp32Id = "esp32_2"
        ),
        Machine(
            id = "machine3",
            roomId = "room1",
            name = "Secarropas 1",
            type = MachineType.SECARROPAS,
            status = MachineState.RESERVADO,
            esp32Id = "esp32_3"
        ),
        Machine(
            id = "machine4",
            roomId = "room1",
            name = "Secarropas 2",
            type = MachineType.SECARROPAS,
            status = MachineState.LIBRE,
            esp32Id = "esp32_4"
        ),
        Machine(
            id = "machine5",
            roomId = "room1",
            name = "Lavarropas 3",
            type = MachineType.LAVARROPAS,
            status = MachineState.MANTENIMIENTO,
            esp32Id = null
        )
    )

    fun getMockBookings(): List<Booking> = listOf(
        Booking(
            id = "booking1",
            machineId = "machine1",
            userId = "current_user_id",
            startDateTime = LocalDateTime.now().plusHours(2),
            endDateTime = LocalDateTime.now().plusHours(4),
            status = BookingStatus.RESERVADO,
            notes = "Ropa delicada"
        ),
        Booking(
            id = "booking2",
            machineId = "machine3",
            userId = "current_user_id",
            startDateTime = LocalDateTime.now().minusHours(1),
            endDateTime = LocalDateTime.now().plusMinutes(30),
            status = BookingStatus.EN_USO,
            actualStartTime = LocalDateTime.now().minusHours(1)
        ),
        Booking(
            id = "booking3",
            machineId = "machine2",
            userId = "current_user_id",
            startDateTime = LocalDateTime.now().minusDays(1),
            endDateTime = LocalDateTime.now().minusDays(1).plusHours(2),
            status = BookingStatus.COMPLETADO,
            actualStartTime = LocalDateTime.now().minusDays(1),
            actualEndTime = LocalDateTime.now().minusDays(1).plusHours(2),
            notes = "Ciclo completado sin problemas"
        ),
        Booking(
            id = "booking4",
            machineId = "machine1",
            userId = "current_user_id",
            startDateTime = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
            endDateTime = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0),
            status = BookingStatus.RESERVADO
        ),
        Booking(
            id = "booking5",
            machineId = "machine4",
            userId = "current_user_id",
            startDateTime = LocalDateTime.now().minusDays(2),
            endDateTime = LocalDateTime.now().minusDays(2).plusHours(2),
            status = BookingStatus.CANCELADO,
            notes = "Cancelado por mantenimiento"
        )
    )
}