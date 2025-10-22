package com.gestor.turnos.domain.usecase

import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.repository.BookingRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CancelBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun execute(bookingId: String, currentVersion: Int): Result<Unit> {

        val bookingResult = bookingRepository.getBookingById(bookingId)
        if (bookingResult is Result.Error) {
            return Result.Error(bookingResult.exception)
        }

        val booking = (bookingResult as Result.Success).data

        if (booking.status != BookingStatus.RESERVADO) {
            return Result.Error(Exception("Solo se pueden cancelar reservas en estado RESERVADO"))
        }

        val now = LocalDateTime.now()
        val timeDifference = java.time.temporal.ChronoUnit.HOURS.between(now, booking.startDateTime)

        if (timeDifference < 1) {
            return Result.Error(Exception("No se puede cancelar con menos de 1 hora de anticipación"))
        }

        return bookingRepository.cancelBooking(bookingId, currentVersion)
    }
}