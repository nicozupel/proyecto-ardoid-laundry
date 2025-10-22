package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.MachineEntity
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.model.MachineType
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {

    @Query("SELECT * FROM machines WHERE isActive = 1 ORDER BY name ASC")
    fun getAllActiveMachines(): Flow<List<MachineEntity>>

    @Query("SELECT * FROM machines WHERE roomId = :roomId AND isActive = 1 ORDER BY name ASC")
    fun getMachinesByRoom(roomId: String): Flow<List<MachineEntity>>

    @Query("SELECT * FROM machines WHERE id = :machineId")
    suspend fun getMachineById(machineId: String): MachineEntity?

    @Query("SELECT * FROM machines WHERE esp32Id = :esp32Id")
    suspend fun getMachineByEsp32Id(esp32Id: String): MachineEntity?

    @Query("SELECT * FROM machines WHERE type = :type AND isActive = 1")
    suspend fun getMachinesByType(type: MachineType): List<MachineEntity>

    @Query("SELECT * FROM machines WHERE status = :status AND isActive = 1")
    suspend fun getMachinesByStatus(status: MachineState): List<MachineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachine(machine: MachineEntity)

    @Update
    suspend fun updateMachine(machine: MachineEntity)

    @Delete
    suspend fun deleteMachine(machine: MachineEntity)

    @Query("UPDATE machines SET status = :newStatus, updatedAt = :updatedAt WHERE id = :machineId")
    suspend fun updateMachineStatus(
        machineId: String,
        newStatus: MachineState,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE machines SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :machineId")
    suspend fun setMachineActive(
        machineId: String,
        isActive: Boolean,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE machines SET esp32Id = :esp32Id, updatedAt = :updatedAt WHERE id = :machineId")
    suspend fun linkEsp32ToMachine(
        machineId: String,
        esp32Id: String,
        updatedAt: Long = System.currentTimeMillis()
    )
}