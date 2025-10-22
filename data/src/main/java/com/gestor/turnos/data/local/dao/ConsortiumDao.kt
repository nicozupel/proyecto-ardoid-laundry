package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.ConsortiumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsortiumDao {

    @Query("SELECT * FROM consortiums ORDER BY name ASC")
    fun getAllConsortiums(): Flow<List<ConsortiumEntity>>

    @Query("SELECT * FROM consortiums WHERE id = :id")
    suspend fun getConsortiumById(id: String): ConsortiumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsortium(consortium: ConsortiumEntity)

    @Update
    suspend fun updateConsortium(consortium: ConsortiumEntity)

    @Delete
    suspend fun deleteConsortium(consortium: ConsortiumEntity)
}