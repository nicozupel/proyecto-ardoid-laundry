package com.gestor.turnos.domain.model

import java.time.LocalDateTime

data class Booking(
    val id: String,
    val machineId: String,
    val userId: String,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
    val status: BookingStatus,
    val version: Int = 1,
    val actualStartTime: LocalDateTime? = null,
    val actualEndTime: LocalDateTime? = null,
    val notes: String? = null
) {
    val durationMinutes: Long
        get() = java.time.temporal.ChronoUnit.MINUTES.between(startDateTime, endDateTime)

    fun isActive(): Boolean = status == BookingStatus.EN_USO

    fun isUpcoming(): Boolean = status == BookingStatus.RESERVADO &&
        startDateTime.isAfter(LocalDateTime.now())

    fun canBeModified(): Boolean = status == BookingStatus.RESERVADO &&
        startDateTime.minusHours(1).isAfter(LocalDateTime.now())

    fun isInGracePeriod(gracePeriodMinutes: Int): Boolean {
        val now = LocalDateTime.now()
        return status == BookingStatus.RESERVADO &&
               now.isAfter(startDateTime) &&
               now.isBefore(startDateTime.plusMinutes(gracePeriodMinutes.toLong()))
    }
}