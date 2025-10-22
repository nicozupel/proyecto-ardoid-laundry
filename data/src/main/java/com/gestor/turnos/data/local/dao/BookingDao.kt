package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.BookingEntity
import com.gestor.turnos.domain.model.BookingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY startDateTime ASC")
    fun getBookingsByUser(userId: String): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE machineId = :machineId AND startDateTime >= :fromTime AND startDateTime < :toTime")
    suspend fun getBookingsForMachineInTimeRange(
        machineId: String,
        fromTime: Long,
        toTime: Long
    ): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    suspend fun getBookingById(bookingId: String): BookingEntity?

    @Query("SELECT * FROM bookings WHERE userId = :userId AND status IN (:statuses) ORDER BY startDateTime DESC")
    suspend fun getBookingsByUserAndStatus(
        userId: String,
        statuses: List<BookingStatus>
    ): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE startDateTime <= :currentTime AND endDateTime > :currentTime AND status = 'EN_USO'")
    suspend fun getActiveBookings(currentTime: Long): List<BookingEntity>

    @Query("SELECT * FROM bookings WHERE startDateTime <= :graceEndTime AND startDateTime > :currentTime AND status = 'RESERVADO'")
    suspend fun getBookingsInGracePeriod(currentTime: Long, graceEndTime: Long): List<BookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :newStatus, updatedAt = :updatedAt WHERE id = :bookingId AND version = :currentVersion")
    suspend fun updateBookingStatus(
        bookingId: String,
        newStatus: BookingStatus,
        currentVersion: Int,
        updatedAt: Long = System.currentTimeMillis()
    ): Int

    @Query("DELETE FROM bookings WHERE userId = :userId")
    suspend fun deleteBookingsByUser(userId: String)

    @Query("SELECT COUNT(*) FROM bookings WHERE userId = :userId AND startDateTime >= :weekStart AND startDateTime < :weekEnd AND status != 'CANCELADO'")
    suspend fun getBookingCountForWeek(userId: String, weekStart: Long, weekEnd: Long): Int
}