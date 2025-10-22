package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.BuildingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BuildingDao {

    @Query("SELECT * FROM buildings WHERE consortiumId = :consortiumId ORDER BY name ASC")
    fun getBuildingsByConsortium(consortiumId: String): Flow<List<BuildingEntity>>

    @Query("SELECT * FROM buildings WHERE id = :id")
    suspend fun getBuildingById(id: String): BuildingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuilding(building: BuildingEntity)

    @Update
    suspend fun updateBuilding(building: BuildingEntity)

    @Delete
    suspend fun deleteBuilding(building: BuildingEntity)
}