package com.gestor.turnos.data.local.database

import androidx.room.TypeConverter
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import com.gestor.turnos.domain.model.UserRole

class Converters {

    @TypeConverter
    fun fromMachineType(type: MachineType): String = type.name

    @TypeConverter
    fun toMachineType(type: String): MachineType = MachineType.valueOf(type)

    @TypeConverter
    fun fromMachineState(state: MachineState): String = state.name

    @TypeConverter
    fun toMachineState(state: String): MachineState = MachineState.valueOf(state)

    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name

    @TypeConverter
    fun toUserRole(role: String): UserRole = UserRole.valueOf(role)

    @TypeConverter
    fun fromBookingStatus(status: BookingStatus): String = status.name

    @TypeConverter
    fun toBookingStatus(status: String): BookingStatus = BookingStatus.valueOf(status)
}