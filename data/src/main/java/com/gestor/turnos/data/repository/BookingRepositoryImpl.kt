package com.gestor.turnos.data.repository

import com.gestor.turnos.core.di.IoDispatcher
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.core.util.asResult
import com.gestor.turnos.data.local.dao.BookingDao
import com.gestor.turnos.data.mapper.BookingMapper
import com.gestor.turnos.domain.model.Booking
import com.gestor.turnos.domain.model.BookingStatus
import com.gestor.turnos.domain.model.WeeklyAvailability
import com.gestor.turnos.domain.repository.BookingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingDao: BookingDao,
    private val bookingMapper: BookingMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BookingRepository {

    override fun getBookingsByUser(userId: String): Flow<Result<List<Booking>>> {
        // Return mock data for demo purposes
        return kotlinx.coroutines.flow.flowOf(MockData.getMockBookings())
            .asResult()
            .flowOn(ioDispatcher)
    }

    override suspend fun getBookingById(bookingId: String): Result<Booking> {
        return withContext(ioDispatcher) {
            try {
                val entity = bookingDao.getBookingById(bookingId)
                if (entity != null) {
                    Result.Success(bookingMapper.toDomain(entity))
                } else {
                    Result.Error(Exception("Booking not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getWeeklyAvailability(
        machineId: String,
        startDate: LocalDate,
        currentUserId: String
    ): Result<WeeklyAvailability> {
        return withContext(ioDispatcher) {
            try {
                // TODO: Implement availability calculation logic
                // This is a placeholder implementation
                Result.Error(Exception("Not implemented yet"))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun createBooking(
        machineId: String,
        userId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Result<Booking> {
        return withContext(ioDispatcher) {
            try {
                val booking = Booking(
                    id = java.util.UUID.randomUUID().toString(),
                    machineId = machineId,
                    userId = userId,
                    startDateTime = startDateTime,
                    endDateTime = endDateTime,
                    status = BookingStatus.RESERVADO
                )

                val entity = bookingMapper.toEntity(booking)
                bookingDao.insertBooking(entity)

                Result.Success(booking)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: BookingStatus,
        currentVersion: Int
    ): Result<Booking> {
        return withContext(ioDispatcher) {
            try {
                val updatedRows = bookingDao.updateBookingStatus(bookingId, newStatus, currentVersion)
                if (updatedRows > 0) {
                    val entity = bookingDao.getBookingById(bookingId)
                    if (entity != null) {
                        Result.Success(bookingMapper.toDomain(entity))
                    } else {
                        Result.Error(Exception("Booking not found after update"))
                    }
                } else {
                    Result.Error(Exception("Update failed - version conflict or booking not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun rescheduleBooking(
        bookingId: String,
        newStartDateTime: LocalDateTime,
        newEndDateTime: LocalDateTime,
        currentVersion: Int
    ): Result<Booking> {
        return withContext(ioDispatcher) {
            try {
                val entity = bookingDao.getBookingById(bookingId)
                if (entity != null) {
                    val updatedEntity = entity.copy(
                        startDateTime = newStartDateTime.toEpochSecond(ZoneOffset.UTC) * 1000,
                        endDateTime = newEndDateTime.toEpochSecond(ZoneOffset.UTC) * 1000,
                        version = currentVersion + 1,
                        updatedAt = System.currentTimeMillis()
                    )
                    bookingDao.updateBooking(updatedEntity)
                    Result.Success(bookingMapper.toDomain(updatedEntity))
                } else {
                    Result.Error(Exception("Booking not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun cancelBooking(bookingId: String, currentVersion: Int): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val updatedRows = bookingDao.updateBookingStatus(bookingId, BookingStatus.CANCELADO, currentVersion)
                if (updatedRows > 0) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Cancel failed - version conflict or booking not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun extendBooking(
        bookingId: String,
        newEndDateTime: LocalDateTime,
        currentVersion: Int
    ): Result<Booking> {
        return withContext(ioDispatcher) {
            try {
                val entity = bookingDao.getBookingById(bookingId)
                if (entity != null) {
                    val updatedEntity = entity.copy(
                        endDateTime = newEndDateTime.toEpochSecond(ZoneOffset.UTC) * 1000,
                        version = currentVersion + 1,
                        updatedAt = System.currentTimeMillis()
                    )
                    bookingDao.updateBooking(updatedEntity)
                    Result.Success(bookingMapper.toDomain(updatedEntity))
                } else {
                    Result.Error(Exception("Booking not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getBookingCountForWeek(userId: String, weekStart: LocalDate): Result<Int> {
        return withContext(ioDispatcher) {
            try {
                val weekEnd = weekStart.plusDays(7)
                val weekStartMillis = weekStart.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000
                val weekEndMillis = weekEnd.atStartOfDay().toEpochSecond(ZoneOffset.UTC) * 1000

                val count = bookingDao.getBookingCountForWeek(userId, weekStartMillis, weekEndMillis)
                Result.Success(count)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun syncBookings(): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                // TODO: Implement API sync logic
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}