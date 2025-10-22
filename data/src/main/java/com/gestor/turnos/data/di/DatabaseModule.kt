package com.gestor.turnos.data.di

import android.content.Context
import com.gestor.turnos.data.local.database.LaundryDatabase
import com.gestor.turnos.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LaundryDatabase {
        return LaundryDatabase.create(context)
    }

    @Provides
    fun provideConsortiumDao(database: LaundryDatabase): ConsortiumDao {
        return database.consortiumDao()
    }

    @Provides
    fun provideBuildingDao(database: LaundryDatabase): BuildingDao {
        return database.buildingDao()
    }

    @Provides
    fun provideRoomDao(database: LaundryDatabase): RoomDao {
        return database.roomDao()
    }

    @Provides
    fun provideMachineDao(database: LaundryDatabase): MachineDao {
        return database.machineDao()
    }

    @Provides
    fun provideApartmentDao(database: LaundryDatabase): ApartmentDao {
        return database.apartmentDao()
    }

    @Provides
    fun provideUserDao(database: LaundryDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideBookingDao(database: LaundryDatabase): BookingDao {
        return database.bookingDao()
    }

    @Provides
    fun providePenaltyDao(database: LaundryDatabase): PenaltyDao {
        return database.penaltyDao()
    }

    @Provides
    fun provideTelemetryDao(database: LaundryDatabase): TelemetryDao {
        return database.telemetryDao()
    }

    @Provides
    fun provideInviteDao(database: LaundryDatabase): InviteDao {
        return database.inviteDao()
    }
}