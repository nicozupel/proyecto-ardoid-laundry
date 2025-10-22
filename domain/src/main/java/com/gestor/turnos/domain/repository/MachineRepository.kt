package com.gestor.turnos.domain.repository

import com.gestor.turnos.core.util.Result
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import kotlinx.coroutines.flow.Flow

interface MachineRepository {

    fun getAllActiveMachines(): Flow<Result<List<Machine>>>

    fun getMachinesByRoom(roomId: String): Flow<Result<List<Machine>>>

    suspend fun getMachineById(machineId: String): Result<Machine>

    suspend fun updateMachineStatus(machineId: String, newStatus: MachineState): Result<Unit>

    suspend fun syncMachines(): Result<Unit>
}