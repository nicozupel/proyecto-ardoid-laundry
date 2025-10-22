package com.gestor.turnos.domain.usecase

import com.gestor.turnos.core.constants.LaundryConstants
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.repository.BookingRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun execute(
        machineId: String,
        userId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Result<Booking> {

        val validationResult = validateBooking(userId, startDateTime, endDateTime)
        if (validationResult is Result.Error) {
            return validationResult
        }

        return bookingRepository.createBooking(machineId, userId, startDateTime, endDateTime)
    }

    private suspend fun validateBooking(
        userId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Result<Unit> {

        val now = LocalDateTime.now()
        if (startDateTime.isBefore(now)) {
            return Result.Error(Exception("No se puede reservar en el pasado"))
        }

        val durationMinutes = ChronoUnit.MINUTES.between(startDateTime, endDateTime)
        if (durationMinutes < LaundryConstants.Limits.MIN_BOOKING_DURATION_MINUTES) {
            return Result.Error(Exception("La duración mínima es ${LaundryConstants.Limits.MIN_BOOKING_DURATION_MINUTES} minutos"))
        }

        if (durationMinutes > LaundryConstants.Limits.MAX_BOOKING_DURATION_MINUTES) {
            return Result.Error(Exception("La duración máxima es ${LaundryConstants.Limits.MAX_BOOKING_DURATION_MINUTES} minutos"))
        }

        val weekStart = startDateTime.toLocalDate().minusDays(startDateTime.dayOfWeek.value - 1L)
        val weeklyBookingsResult = bookingRepository.getBookingCountForWeek(userId, weekStart)

        if (weeklyBookingsResult is Result.Success) {
            if (weeklyBookingsResult.data >= LaundryConstants.Limits.MAX_BOOKINGS_PER_WEEK) {
                return Result.Error(Exception("Se alcanzó el límite de ${LaundryConstants.Limits.MAX_BOOKINGS_PER_WEEK} reservas por semana"))
            }
        }

        return Result.Success(Unit)
    }
}