package com.gestor.turnos.data.di

import com.gestor.turnos.data.repository.BookingRepositoryImpl
import com.gestor.turnos.data.repository.MachineRepositoryImpl
import com.gestor.turnos.domain.repository.BookingRepository
import com.gestor.turnos.domain.repository.MachineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindMachineRepository(
        machineRepositoryImpl: MachineRepositoryImpl
    ): MachineRepository
}