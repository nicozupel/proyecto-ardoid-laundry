package com.gestor.turnos.data.mapper

import com.gestor.turnos.data.local.entity.BookingEntity
import com.gestor.turnos.domain.model.Booking
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class BookingMapper @Inject constructor() {

    fun toDomain(entity: BookingEntity): Booking {
        return Booking(
            id = entity.id,
            machineId = entity.machineId,
            userId = entity.userId,
            startDateTime = entity.startDateTime.toLocalDateTime(),
            endDateTime = entity.endDateTime.toLocalDateTime(),
            status = entity.status,
            version = entity.version,
            actualStartTime = entity.actualStartTime?.toLocalDateTime(),
            actualEndTime = entity.actualEndTime?.toLocalDateTime(),
            notes = entity.notes
        )
    }

    fun toEntity(domain: Booking): BookingEntity {
        return BookingEntity(
            id = domain.id,
            machineId = domain.machineId,
            userId = domain.userId,
            startDateTime = domain.startDateTime.toEpochMilli(),
            endDateTime = domain.endDateTime.toEpochMilli(),
            status = domain.status,
            version = domain.version,
            actualStartTime = domain.actualStartTime?.toEpochMilli(),
            actualEndTime = domain.actualEndTime?.toEpochMilli(),
            notes = domain.notes
        )
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
    }

    private fun LocalDateTime.toEpochMilli(): Long {
        return this.toInstant(ZoneOffset.UTC).toEpochMilli()
    }
}