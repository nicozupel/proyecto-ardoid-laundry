package com.gestor.turnos.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class AvailabilitySlot(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val isAvailable: Boolean,
    val bookingId: String? = null,
    val userId: String? = null,
    val isOwnBooking: Boolean = false
)

data class DayAvailability(
    val date: LocalDate,
    val slots: List<AvailabilitySlot>
) {
    fun getAvailableSlots(): List<AvailabilitySlot> = slots.filter { it.isAvailable }

    fun hasAvailableSlots(): Boolean = slots.any { it.isAvailable }
}

data class WeeklyAvailability(
    val machineId: String,
    val startDate: LocalDate,
    val days: List<DayAvailability>
) {
    fun getDayAvailability(date: LocalDate): DayAvailability? = days.find { it.date == date }
}