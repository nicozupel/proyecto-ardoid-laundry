package com.gestor.turnos.user.calendar

import com.gestor.turnos.core.constants.LaundryConstants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class TimeSlot(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val isAvailable: Boolean = true,
    val isSelected: Boolean = false,
    val bookingId: String? = null,
    val isOwnBooking: Boolean = false
)

data class DayColumn(
    val date: LocalDate,
    val slots: List<TimeSlot>
)

object CalendarUtils {

    fun generateWeekDays(startDate: LocalDate): List<LocalDate> {
        return (0..6).map { startDate.plusDays(it.toLong()) }
    }

    fun generateTimeSlots(
        date: LocalDate,
        startHour: Int = LaundryConstants.OperatingHours.OPEN_HOUR,
        endHour: Int = LaundryConstants.OperatingHours.CLOSE_HOUR,
        granularityMinutes: Int = LaundryConstants.Timing.GRID_GRANULARITY_MINUTES
    ): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        val startTime = date.atTime(startHour, 0)
        val endTime = date.atTime(endHour, 0)

        var currentTime = startTime
        while (currentTime.isBefore(endTime)) {
            val slotEnd = currentTime.plusMinutes(granularityMinutes.toLong())
            slots.add(
                TimeSlot(
                    startTime = currentTime,
                    endTime = slotEnd,
                    isAvailable = true
                )
            )
            currentTime = slotEnd
        }

        return slots
    }

    fun getTotalSlotsCount(
        startHour: Int = LaundryConstants.OperatingHours.OPEN_HOUR,
        endHour: Int = LaundryConstants.OperatingHours.CLOSE_HOUR,
        granularityMinutes: Int = LaundryConstants.Timing.GRID_GRANULARITY_MINUTES
    ): Int {
        val totalMinutes = (endHour - startHour) * 60
        return totalMinutes / granularityMinutes
    }

    fun getSlotIndex(
        time: LocalTime,
        startHour: Int = LaundryConstants.OperatingHours.OPEN_HOUR,
        granularityMinutes: Int = LaundryConstants.Timing.GRID_GRANULARITY_MINUTES
    ): Int {
        val startTime = LocalTime.of(startHour, 0)
        val minutesDiff = ChronoUnit.MINUTES.between(startTime, time)
        return (minutesDiff / granularityMinutes).toInt()
    }

    fun formatTimeLabel(time: LocalTime): String {
        return String.format("%02d:%02d", time.hour, time.minute)
    }

    fun isValidSlotSelection(
        selectedSlots: List<TimeSlot>,
        minDurationMinutes: Int = LaundryConstants.Limits.MIN_BOOKING_DURATION_MINUTES,
        maxDurationMinutes: Int = LaundryConstants.Limits.MAX_BOOKING_DURATION_MINUTES
    ): Boolean {
        if (selectedSlots.isEmpty()) return false

        val totalMinutes = selectedSlots.size * LaundryConstants.Timing.GRID_GRANULARITY_MINUTES
        return totalMinutes >= minDurationMinutes && totalMinutes <= maxDurationMinutes
    }

    fun areConsecutiveSlots(slots: List<TimeSlot>): Boolean {
        if (slots.size <= 1) return true

        val sortedSlots = slots.sortedBy { it.startTime }
        for (i in 1 until sortedSlots.size) {
            if (sortedSlots[i-1].endTime != sortedSlots[i].startTime) {
                return false
            }
        }
        return true
    }
}