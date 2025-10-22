package com.gestor.turnos.data.repository

import com.gestor.turnos.core.di.IoDispatcher
import com.gestor.turnos.core.util.Result
import com.gestor.turnos.core.util.asResult
import com.gestor.turnos.data.local.dao.MachineDao
import com.gestor.turnos.data.mapper.MachineMapper
import com.gestor.turnos.domain.model.Machine
import com.gestor.turnos.domain.model.MachineState
import com.gestor.turnos.domain.repository.MachineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MachineRepositoryImpl @Inject constructor(
    private val machineDao: MachineDao,
    private val machineMapper: MachineMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MachineRepository {

    override fun getAllActiveMachines(): Flow<Result<List<Machine>>> {
        // Return mock data for demo purposes
        return kotlinx.coroutines.flow.flowOf(MockData.getMockMachines())
            .asResult()
            .flowOn(ioDispatcher)
    }

    override fun getMachinesByRoom(roomId: String): Flow<Result<List<Machine>>> {
        return machineDao.getMachinesByRoom(roomId)
            .map { entities -> entities.map { machineMapper.toDomain(it) } }
            .asResult()
            .flowOn(ioDispatcher)
    }

    override suspend fun getMachineById(machineId: String): Result<Machine> {
        return withContext(ioDispatcher) {
            try {
                val entity = machineDao.getMachineById(machineId)
                if (entity != null) {
                    Result.Success(machineMapper.toDomain(entity))
                } else {
                    Result.Error(Exception("Machine not found"))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun updateMachineStatus(machineId: String, newStatus: MachineState): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                machineDao.updateMachineStatus(machineId, newStatus)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun syncMachines(): Result<Unit> {
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