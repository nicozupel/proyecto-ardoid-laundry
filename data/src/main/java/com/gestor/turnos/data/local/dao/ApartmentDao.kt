package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.ApartmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ApartmentDao {

    @Query("SELECT * FROM apartments WHERE buildingId = :buildingId ORDER BY floor ASC, number ASC")
    fun getApartmentsByBuilding(buildingId: String): Flow<List<ApartmentEntity>>

    @Query("SELECT * FROM apartments WHERE id = :id")
    suspend fun getApartmentById(id: String): ApartmentEntity?

    @Query("SELECT * FROM apartments WHERE buildingId = :buildingId AND floor = :floor AND number = :number")
    suspend fun getApartmentByLocation(buildingId: String, floor: Int, number: String): ApartmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApartment(apartment: ApartmentEntity)

    @Update
    suspend fun updateApartment(apartment: ApartmentEntity)

    @Delete
    suspend fun deleteApartment(apartment: ApartmentEntity)
}