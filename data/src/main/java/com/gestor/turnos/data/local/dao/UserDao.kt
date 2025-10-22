package com.gestor.turnos.data.local.dao

import androidx.room.*
import com.gestor.turnos.data.local.entity.UserEntity
import com.gestor.turnos.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email AND isActive = 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE apartmentId = :apartmentId AND isActive = 1")
    suspend fun getUsersByApartment(apartmentId: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE role = :role AND isActive = 1")
    suspend fun getUsersByRole(role: UserRole): List<UserEntity>

    @Query("SELECT * FROM users WHERE isActive = 1 ORDER BY lastName, firstName")
    fun getAllActiveUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("UPDATE users SET fcmToken = :token, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateFcmToken(
        userId: String,
        token: String,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE users SET lastLoginAt = :loginTime, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun updateLastLogin(
        userId: String,
        loginTime: Long,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE users SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :userId")
    suspend fun setUserActive(
        userId: String,
        isActive: Boolean,
        updatedAt: Long = System.currentTimeMillis()
    )
}