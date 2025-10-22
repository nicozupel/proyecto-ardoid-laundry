package com.gestor.turnos.domain.usecase

import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.WeeklyAvailability
import com.gestor.turnos.domain.repository.BookingRepository
import java.time.LocalDate
import javax.inject.Inject

class GetAvailabilityUseCase @Inject constructor(
    private val bookingRepository: BookingRepository
) {
    suspend fun execute(
        machineId: String,
        startDate: LocalDate,
        currentUserId: String
    ): Result<WeeklyAvailability> {
        return bookingRepository.getWeeklyAvailability(machineId, startDate, currentUserId)
    }
}