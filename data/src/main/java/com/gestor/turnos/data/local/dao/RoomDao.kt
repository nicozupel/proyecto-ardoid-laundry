package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Query("SELECT * FROM rooms WHERE buildingId = :buildingId ORDER BY name ASC")
    fun getRoomsByBuilding(buildingId: String): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoomById(id: String): RoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity)

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Delete
    suspend fun deleteRoom(room: RoomEntity)
}