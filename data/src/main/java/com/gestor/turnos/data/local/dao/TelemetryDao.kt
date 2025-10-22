package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.TelemetryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TelemetryDao {

    @Query("SELECT * FROM telemetry WHERE esp32Id = :esp32Id ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestTelemetryForEsp32(esp32Id: String, limit: Int = 100): List<TelemetryEntity>

    @Query("SELECT * FROM telemetry WHERE esp32Id = :esp32Id ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestTelemetryForEsp32Single(esp32Id: String): TelemetryEntity?

    @Query("SELECT * FROM telemetry WHERE esp32Id = :esp32Id AND timestamp >= :fromTime AND timestamp <= :toTime ORDER BY timestamp ASC")
    suspend fun getTelemetryInTimeRange(
        esp32Id: String,
        fromTime: Long,
        toTime: Long
    ): List<TelemetryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTelemetry(telemetry: TelemetryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTelemetryBatch(telemetry: List<TelemetryEntity>)

    @Query("DELETE FROM telemetry WHERE timestamp < :olderThan")
    suspend fun deleteOldTelemetry(olderThan: Long)

    @Query("DELETE FROM telemetry WHERE esp32Id = :esp32Id AND timestamp < :olderThan")
    suspend fun deleteOldTelemetryForEsp32(esp32Id: String, olderThan: Long)
}