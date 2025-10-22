package com.gestor.turnos.domain.usecase

import com.gestor.turnos.core.constants.LaundryConstants
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.repository.BookingRepository
import java.time.LocalDateTime
import javax.inject.Inject

class StartBookingUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun execute(bookingId: String, currentVersion: Int): Result<Booking> {

        val bookingResult = bookingRepository.getBookingById(bookingId)
        if (bookingResult is Result.Error) {
            return Result.Error(bookingResult.exception)
        }

        val booking = (bookingResult as Result.Success).data

        if (booking.status != BookingStatus.RESERVADO) {
            return Result.Error(Exception("Solo se pueden iniciar reservas en estado RESERVADO"))
        }

        val now = LocalDateTime.now()
        val gracePeriodEnd = booking.startDateTime.plusMinutes(LaundryConstants.Timing.GRACE_PERIOD_MINUTES.toLong())

        if (now.isBefore(booking.startDateTime)) {
            return Result.Error(Exception("No se puede iniciar antes del horario reservado"))
        }

        if (now.isAfter(gracePeriodEnd)) {
            return Result.Error(Exception("Se venció el período de gracia para iniciar el turno"))
        }

        return bookingRepository.updateBookingStatus(bookingId, BookingStatus.EN_USO, currentVersion)
    }
}