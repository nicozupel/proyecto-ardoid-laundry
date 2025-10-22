package com.gestor.turnos.domain.repository

import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.WeeklyAvailability
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface BookingRepository {

    fun getBookingsByUser(userId: String): Flow<Result<List<Booking>>>

    suspend fun getBookingById(bookingId: String): Result<Booking>

    suspend fun getWeeklyAvailability(
        machineId: String,
        startDate: LocalDate,
        currentUserId: String
    ): Result<WeeklyAvailability>

    suspend fun createBooking(
        machineId: String,
        userId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Result<Booking>

    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: BookingStatus,
        currentVersion: Int
    ): Result<Booking>

    suspend fun rescheduleBooking(
        bookingId: String,
        newStartDateTime: LocalDateTime,
        newEndDateTime: LocalDateTime,
        currentVersion: Int
    ): Result<Booking>

    suspend fun cancelBooking(bookingId: String, currentVersion: Int): Result<Unit>

    suspend fun extendBooking(
        bookingId: String,
        newEndDateTime: LocalDateTime,
        currentVersion: Int
    ): Result<Booking>

    suspend fun getBookingCountForWeek(userId: String, weekStart: LocalDate): Result<Int>

    suspend fun syncBookings(): Result<Unit>
}