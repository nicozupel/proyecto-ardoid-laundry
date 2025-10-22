package com.gestor.turnos.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

fun LocalDateTime.toTimeSlot(durationMinutes: Int): TimeSlot {
    return TimeSlot(
        start = this,
        end = this.plusMinutes(durationMinutes.toLong())
    )
}

fun LocalDate.atTime(hour: Int, minute: Int = 0): LocalDateTime {
    return this.atTime(LocalTime.of(hour, minute))
}

fun LocalDateTime.isInGracePeriod(gracePeriodMinutes: Int): Boolean {
    val now = LocalDateTime.now()
    return now.isAfter(this) && now.isBefore(this.plusMinutes(gracePeriodMinutes.toLong()))
}

fun LocalDateTime.minutesUntil(other: LocalDateTime): Long {
    return ChronoUnit.MINUTES.between(this, other)
}

fun LocalDateTime.formatTime(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalDateTime.formatDateTime(): String {
    return this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
}

fun LocalDate.formatDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

data class TimeSlot(
    val start: LocalDateTime,
    val end: LocalDateTime
) {
    val durationMinutes: Long
        get() = ChronoUnit.MINUTES.between(start, end)

    fun overlaps(other: TimeSlot): Boolean {
        return start.isBefore(other.end) && end.isAfter(other.start)
    }

    fun contains(dateTime: LocalDateTime): Boolean {
        return !dateTime.isBefore(start) && dateTime.isBefore(end)
    }

    fun isValid(): Boolean {
        return start.isBefore(end)
    }
}