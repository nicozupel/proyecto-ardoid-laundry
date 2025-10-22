package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.InviteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InviteDao {

    @Query("SELECT * FROM invites WHERE code = :code AND isActive = 1 AND expiresAt > :currentTime")
    suspend fun getValidInviteByCode(code: String, currentTime: Long): InviteEntity?

    @Query("SELECT * FROM invites WHERE apartmentId = :apartmentId AND isActive = 1 ORDER BY createdAt DESC")
    fun getInvitesByApartment(apartmentId: String): Flow<List<InviteEntity>>

    @Query("SELECT * FROM invites WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getInvitesByCreator(userId: String): Flow<List<InviteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvite(invite: InviteEntity)

    @Update
    suspend fun updateInvite(invite: InviteEntity)

    @Query("UPDATE invites SET usedByUserId = :userId, usedAt = :usedAt, isActive = 0 WHERE code = :code")
    suspend fun markInviteAsUsed(code: String, userId: String, usedAt: Long)

    @Query("UPDATE invites SET isActive = 0 WHERE expiresAt <= :currentTime")
    suspend fun deactivateExpiredInvites(currentTime: Long)

    @Delete
    suspend fun deleteInvite(invite: InviteEntity)
}