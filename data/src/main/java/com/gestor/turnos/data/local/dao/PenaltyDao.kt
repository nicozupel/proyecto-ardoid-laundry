package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.PenaltyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PenaltyDao {

    @Query("SELECT * FROM penalties WHERE userId = :userId AND isActive = 1 AND expiresAt > :currentTime")
    suspend fun getActivePenaltiesByUser(userId: String, currentTime: Long): List<PenaltyEntity>

    @Query("SELECT SUM(points) FROM penalties WHERE userId = :userId AND isActive = 1 AND expiresAt > :currentTime")
    suspend fun getTotalPenaltyPointsByUser(userId: String, currentTime: Long): Int?

    @Query("SELECT * FROM penalties WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPenaltyHistoryByUser(userId: String): Flow<List<PenaltyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPenalty(penalty: PenaltyEntity)

    @Update
    suspend fun updatePenalty(penalty: PenaltyEntity)

    @Query("UPDATE penalties SET isActive = 0 WHERE userId = :userId AND expiresAt <= :currentTime")
    suspend fun expireOldPenalties(userId: String, currentTime: Long)

    @Query("UPDATE penalties SET isActive = 0 WHERE expiresAt <= :currentTime")
    suspend fun expireAllOldPenalties(currentTime: Long)

    @Delete
    suspend fun deletePenalty(penalty: PenaltyEntity)
}