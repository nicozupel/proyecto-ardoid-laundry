package com.gestor.turnos.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.gestor.turnos.data.local.dao.*
import com.gestor.turnos.data.local.entity.*

@Database(
    entities = [
        ConsortiumEntity::class,
        BuildingEntity::class,
        RoomEntity::class,
        MachineEntity::class,
        ApartmentEntity::class,
        UserEntity::class,
        BookingEntity::class,
        PenaltyEntity::class,
        TelemetryEntity::class,
        InviteEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LaundryDatabase : RoomDatabase() {

    abstract fun consortiumDao(): ConsortiumDao
    abstract fun buildingDao(): BuildingDao
    abstract fun roomDao(): RoomDao
    abstract fun machineDao(): MachineDao
    abstract fun apartmentDao(): ApartmentDao
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao
    abstract fun penaltyDao(): PenaltyDao
    abstract fun telemetryDao(): TelemetryDao
    abstract fun inviteDao(): InviteDao

    companion object {
        const val DATABASE_NAME = "laundry_database"

        fun create(context: Context): LaundryDatabase {
            return Room.databaseBuilder(
                context,
                LaundryDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}